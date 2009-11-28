package net.jgf.tools.codegenerators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Creates a converter class to convert all the properties from one bean to
 * another, as long as they are present in both beans. If a property is missing,
 * there will be a message stating which property is missing in which bean.
 * @author Mark
 * @version 1.0
 */
public final class GenerateBeanTranslator {

    private static String translatorPackage;

    /**
     * private constructor.
     */
    private GenerateBeanTranslator(String translatorPackage) {
        GenerateBeanTranslator.translatorPackage = translatorPackage;
    }

    /**
     * Only public method, used to startup the entire creation process.
     * @param bean1 first bean.
     * @param bean2 second bean.
     * @param translatorPackageParam package to contain the bean translator.
     * @return translator corresponding to the supplied classes.
     */
    @SuppressWarnings("unchecked")
    public static Translator convert(Class bean1, Class bean2, String translatorPackageParam) {
        GenerateBeanTranslator translator = new GenerateBeanTranslator(translatorPackageParam);
        return translator.convertBean(bean1, bean2);
    }

    @SuppressWarnings("unchecked")
    private List < Method > determineGetters(Class bean) {
        List < Method > getters = new ArrayList < Method >();
        Method[] methods = bean.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                getters.add(method);
            }
        }
        return getters;
    }

    @SuppressWarnings("unchecked")
    private List < Method > determineSetters(Class bean) {
        List < Method > setters = new ArrayList < Method >();
        Method[] methods = bean.getMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("set")) {
                setters.add(method);
            }
        }
        return setters;
    }

    @SuppressWarnings("unchecked")
    private Translator convertBean(Class beanIn, Class beanOut) {
        String translatorName = generateName(beanIn.getName(), beanOut.getName());
        StringBuffer translatorContent = new StringBuffer();
        generateHeaders(translatorName, translatorContent);
        generateMethod(beanIn, beanOut, translatorContent);
        Translator result = new Translator();
        result.setTranslatorContent(translatorContent.toString());
        result.setTranslatorName(translatorName);
        return result;
    }

    @SuppressWarnings("unchecked")
    private void generateMethod(Class beanIn, Class beanOut, StringBuffer translatorContent) {
        translatorContent.append("    /**\n");
        translatorContent.append("     * Translation method. Translates " + beanIn.getName()
                + " into " + beanOut.getName() + ".\n");
        translatorContent.append("     * @param beanInParam bean to translate.\n");
        translatorContent.append("     * @return translated parameter.\n");
        translatorContent.append("     */\n");
        translatorContent.append("    public Object translate(Object beanInParam) {\n");
        translatorContent.append("        " + beanIn.getName() + " beanIn = (" + beanIn.getName() + ") beanInParam;\n");
        translatorContent.append("        " + beanOut.getName() + " beanOut = new " + beanOut.getName() + "();\n");

        List < Method > beanInGetters = determineGetters(beanIn);
        List < Method > beanOutSetters = determineSetters(beanOut);
        List < Method > beanInMatchingGetters = new ArrayList < Method >();
        List < Method > beanOutMatchingSetters = new ArrayList < Method >();
        List < Method > beanInMissingGetters = new ArrayList < Method >();
        List < Method > beanOutMissingSetters = new ArrayList < Method >();

        determineMatchinProperties(beanInGetters, beanOutSetters, beanInMatchingGetters, beanOutMatchingSetters);
        determineMissingProperties(beanInGetters, beanOutSetters, beanInMatchingGetters, beanOutMatchingSetters,
                beanInMissingGetters, beanOutMissingSetters);

        beanInGetters.clear();
        beanOutSetters.clear();

        generateCopy(translatorContent, beanInMatchingGetters, beanOutMatchingSetters);

        translatorContent.append("        return beanOut;\n");
        translatorContent.append("    }\n\n");

        generateComments(beanIn, beanOut, translatorContent, beanInMissingGetters, beanOutMissingSetters);

        translatorContent.append("\n\n}");
    }

    @SuppressWarnings("unchecked")
    private void generateComments(Class beanIn, Class beanOut, StringBuffer translatorContent,
            List < Method > beanInMissingGetters, List < Method > beanOutMissingSetters) {
        translatorContent.append("//====Missing in ");
        translatorContent.append(beanOut.getName());
        translatorContent.append("\n//");
        for (Method s : beanOutMissingSetters) {
            translatorContent.append(s.getName());
            translatorContent.append(" has no getter in ");
            translatorContent.append(beanIn.getName());
            translatorContent.append("\n//");
        }

        translatorContent.append("\n\n//====Missing in ");
        translatorContent.append(beanIn.getName());
        translatorContent.append("\n//");
        for (Method g : beanInMissingGetters) {
            translatorContent.append(g.getName());
            translatorContent.append(" has no setter in ");
            translatorContent.append(beanOut.getName());
            translatorContent.append("\n//");
        }
    }

    private void generateCopy(StringBuffer translatorContent, List < Method > beanInMatchingGetters,
            List < Method > beanOutMatchingSetters) {
        for (Method g : beanInMatchingGetters) {
            Method s = findSetter(beanOutMatchingSetters, g);
            translatorContent.append("        beanOut." + s.getName() + "(beanIn." + g.getName() + "());\n");
        }
    }

    private void determineMissingProperties(List < Method > beanInGetters, List < Method > beanOutSetters,
            List < Method > beanInMatchingGetters, List < Method > beanOutMatchingSetters,
            List < Method > beanInMissingGetters, List < Method > beanOutMissingSetters) {
        for (Method g : beanInGetters) {
            if (!beanInMatchingGetters.contains(g)) {
                beanInMissingGetters.add(g);
            }
        }

        for (Method s : beanOutSetters) {
            if (!beanOutMatchingSetters.contains(s)) {
                beanOutMissingSetters.add(s);
            }
        }
    }

    private void determineMatchinProperties(List < Method > beanInGetters, List < Method > beanOutSetters,
            List < Method > beanInMatchingGetters, List < Method > beanOutMatchingSetters) {
        for (Method g : beanInGetters) {
            Method matchingMethod = findSetter(beanOutSetters, g);
            if (matchingMethod != null) {
                beanInMatchingGetters.add(g);
            }
        }

        for (Method s : beanOutSetters) {
            Method matchingMethod = findGetter(beanInGetters, s);
            if (matchingMethod != null) {
                beanOutMatchingSetters.add(s);
            }
        }
    }

    private Method findSetter(List < Method > setters, Method getter) {
        Method matchingMethod = null;
        String setter = "s" + getter.getName().substring(1);
        for (Method s : setters) {
            if (s.getName().equals(setter) && s.getParameterTypes().length == 1 && getter.getReturnType() != null
                    && s.getParameterTypes()[0].equals(getter.getReturnType())) {
                matchingMethod = s;
            }
        }
        return matchingMethod;
    }

    private Method findGetter(List < Method > getters, Method setter) {
        Method matchingMethod = null;
        String getter = "g" + setter.getName().substring(1);
        for (Method g : getters) {
            if (g.getName().equals(getter) && setter.getParameterTypes().length == 1 && g.getReturnType() != null
                    && setter.getParameterTypes()[0].equals(g.getReturnType())) {
                matchingMethod = g;
            }
        }
        return matchingMethod;
    }

    private void generateHeaders(String translatorName, StringBuffer translatorContent) {
        translatorContent.append("package " + GenerateBeanTranslator.translatorPackage + ";\n\n");
        translatorContent.append("/**\n");
        translatorContent.append(" * Class to translate between two beans.\n");
        translatorContent.append(" * @author Mark Schrijver\n");
        translatorContent.append(" * @version 1.0\n");
        translatorContent.append(" */\n");
        translatorContent.append("public final class " + translatorName
                + " implements net.jgf.translators.Translator {\n\n");
        translatorContent.append("    /**\n");
        translatorContent.append("     * Constructor.\n");
        translatorContent.append("     */\n");
        translatorContent.append("    public " + translatorName + "() {\n");
        translatorContent.append("    }\n\n");
    }

    private String generateName(String nameIn, String nameOut) {
        StringBuffer name = new StringBuffer();
        name.append("Translate");
        name.append(nameIn.substring(nameIn.lastIndexOf('.') + 1));
        name.append("To");
        name.append(nameOut.substring(nameOut.lastIndexOf('.') + 1));
        return name.toString();
    }
    
    /**
     * Class containing the actual translator data, name and content.
     * @author Schrijver
     * @version 1.0
     */
    public class Translator {
        private String translatorContent;
        private String translatorName;

        /**
         * Constructor.
         */
        public Translator() {
        }
        
        /**
         * Get the translator content.
         * @return The translator content.
         */
        public String getTranslatorContent() {
            return translatorContent;
        }
        
        /**
         * Set the translator content.
         * @param translatorContent The translator content.
         */
        public void setTranslatorContent(String translatorContent) {
            this.translatorContent = translatorContent;
        }
        
        /**
         * Get the translator name.
         * @return the translator name.
         */
        public String getTranslatorName() {
            return translatorName;
        }
        
        /**
         * Set the translator name.
         * @param translatorName the translator name.
         */
        public void setTranslatorName(String translatorName) {
            this.translatorName = translatorName;
        }
    }
}
