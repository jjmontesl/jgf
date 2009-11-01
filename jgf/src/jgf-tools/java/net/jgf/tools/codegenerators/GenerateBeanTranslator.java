package net.jgf.tools.codegenerators;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Creates a converter class to convert all the properties from one bean to
 * another, as long as they are present in both beans. If a property is missing,
 * there will be a message stating which property is missing in which bean.
 * @author Mark
 */
public class GenerateBeanTranslator {

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
     */
    @SuppressWarnings("unchecked")
    public static Translator convert(Class bean1, Class bean2, String translatorPackage) {
        GenerateBeanTranslator translator = new GenerateBeanTranslator(translatorPackage);
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
        translatorContent.append("public Object translate(Object beanInParam) {\n\t\t");
        translatorContent.append(beanIn.getName());
        translatorContent.append(" beanIn = (");
        translatorContent.append(beanIn.getName());
        translatorContent.append(") beanInParam;\n\t\t");
        translatorContent.append(beanOut.getName());
        translatorContent.append(" beanOut = new ");
        translatorContent.append(beanOut.getName());
        translatorContent.append("();\n\t\t");

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

        translatorContent.append("return beanOut;\n\t}");

        generateComments(beanIn, beanOut, translatorContent, beanInMissingGetters, beanOutMissingSetters);

        translatorContent.append("\n\n}");
    }

    @SuppressWarnings("unchecked")
    private void generateComments(Class beanIn, Class beanOut, StringBuffer translatorContent,
            List < Method > beanInMissingGetters, List < Method > beanOutMissingSetters) {
        translatorContent.append("\n\n//====Missing in ");
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
            translatorContent.append("beanOut.");
            translatorContent.append(s.getName());
            translatorContent.append("(beanIn.");
            translatorContent.append(g.getName());
            translatorContent.append("());\n\t\t");
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
        translatorContent.append("package ");
        translatorContent.append(GenerateBeanTranslator.translatorPackage);
        translatorContent.append(";\n\npublic final class ");
        translatorContent.append(translatorName);
        translatorContent.append(" implements net.jgf.translators.Translator {\n\n\tpublic ");
        translatorContent.append(translatorName);
        translatorContent.append("(){\n\t}\n\n\t ");
    }

    private String generateName(String nameIn, String nameOut) {
        StringBuffer name = new StringBuffer();
        name.append("Translate");
        name.append(nameIn.substring(nameIn.lastIndexOf('.') + 1));
        name.append("To");
        name.append(nameOut.substring(nameOut.lastIndexOf('.') + 1));
        return name.toString();
    }
    
    public class Translator {
        private String translatorContent;
        private String translatorName;
        
        public String getTranslatorContent() {
            return translatorContent;
        }
        
        public void setTranslatorContent(String translatorContent) {
            this.translatorContent = translatorContent;
        }
        
        public String getTranslatorName() {
            return translatorName;
        }
        
        public void setTranslatorName(String translatorName) {
            this.translatorName = translatorName;
        }
    }
}
