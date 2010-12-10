package net.jgf.tools.codegenerators.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.jgf.tools.codegenerators.GenerateBeanTranslator;
import net.jgf.tools.codegenerators.GenerateBeanTranslator.Translator;

/**
 * Task which is called from ant to generate bean translators.
 * 
 * @author Schrijver
 * @version 1.0
 */
public class GenerateBeanTranslatorTask extends Task {

    private String destDir;

    private List<Bean> beans = new ArrayList<Bean>();

    private String mapPackage;

    /**
     * Constructor.
     */
    public GenerateBeanTranslatorTask() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void execute() {
        File dir = new File(destDir + "/" + mapPackage.replace('.', '/'));
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new BuildException("Unable to create package dir.");
            }
        }
        File translatorMapFile = new File(dir, "TranslatorMap.java");
        FileWriter writer;
        try {
            writer = new FileWriter(translatorMapFile);
            writeTranslatorMapheaders(writer);
        } catch (IOException e1) {
            throw new BuildException(e1);
        }
        for (Bean bean : beans) {
            try {
                Class beanInClass = GenerateBeanTranslatorTask.class.getClassLoader().loadClass(
                        bean.getBeanIn());
                Class beanOutClass = GenerateBeanTranslatorTask.class.getClassLoader().loadClass(
                        bean.getBeanOut());
                Translator translator = GenerateBeanTranslator.convert(beanInClass, beanOutClass,
                        bean.getPackageName());
                storeTranslator(bean.getPackageName(), translator);
                writer.write("        translatorsByKey.put(\"" + beanInClass.getName() + "\",\n");
                writer.write("                        new " + bean.getPackageName() + "."
                        + translator.getTranslatorName() + "());\n");
                if (bean.isInverted()) {
                    translator = GenerateBeanTranslator.convert(beanOutClass, beanInClass, bean
                            .getPackageName());
                    storeTranslator(bean.getPackageName(), translator);
                    writer.write("        translatorsByKey.put(\"" + beanOutClass.getName()
                            + "\",\n");
                    writer.write("                        new " + bean.getPackageName() + "."
                            + translator.getTranslatorName() + "());\n");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writeTranslatorMapFooter(writer);
            writer.close();
        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    private void writeTranslatorMapheaders(FileWriter writer) throws IOException {
        writer.write("package " + mapPackage + ";\n\n");
        writer.write("import java.util.Map;\n");
        writer.write("import java.util.LinkedHashMap;\n");
        writer.write("import net.jgf.translators.Translator;\n\n");
        writer.write("/**\n");
        writer.write(" * Main translator class. Only has one method, translate.\n");
        writer.write(" * @author Mark Schrijver\n");
        writer.write(" * @version 1.0\n");
        writer.write(" */\n");
        writer.write("public final class TranslatorMap {\n\n");
        writer.write("    private static final Map <String, Translator> translatorsByKey = "
                + "new LinkedHashMap <String, Translator>();\n\n");
        writer.write("    static {\n");
    }

    private void writeTranslatorMapFooter(FileWriter writer) throws IOException {
        writer.write("    }\n\n");
        writer.write("    /**\n");
        writer.write("     * Constructor.\n");
        writer.write("     */\n");
        writer.write("    private TranslatorMap() {\n");
        writer.write("    }\n\n");
        writer.write("    /**\n");
        writer
                .write("     * Selects the correct translator and uses that translator to translate the"
                        + " supplied message.\n");
        writer
                .write("     * Only one translator is possible per bean class. If no translator is available"
                        + " null is returned.\n");
        writer.write("     * @param beanToTranslate the bean that needs to be translated.\n");
        writer
                .write("     * @return the translated bean or null if no translator was available.\n");
        writer.write("     */\n");
        writer.write("    public static Object translate(Object beanToTranslate) {\n");
        writer
                .write("        Translator translator = translatorsByKey.get(beanToTranslate.getClass().getName());\n");
        writer.write("        if (translator == null) {\n");
        writer.write("            return null;\n");
        writer.write("        }\n");
        writer.write("        return translator.translate(beanToTranslate);\n");
        writer.write("    }\n}");
    }

    private void storeTranslator(String packageName, Translator translator) throws IOException {
        File dir = new File(destDir + "/" + packageName.replace('.', '/'));
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Unable to create package dir.");
            }
        }
        File translatorFile = new File(dir, translator.getTranslatorName() + ".java");
        FileWriter writer = new FileWriter(translatorFile);
        writer.write(translator.getTranslatorContent());
        writer.close();
    }

    /**
     * Sets the destination directory.
     * 
     * @param destDir
     *            directory.
     */
    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    /**
     * Sets the map package.
     * 
     * @param mapPackage
     *            the map package..
     */
    public void setMapPackage(String mapPackage) {
        this.mapPackage = mapPackage;
    }

    /**
     * Creates the bean config based on the sub-tag.
     * 
     * @return the bean created.
     */
    public Bean createBean() {
        Bean bean = new Bean();
        beans.add(bean);
        return bean;
    }

    /**
     * The Bean class. Contains all that is necessary to create the translator.
     * 
     * @author Schrijver
     * @version 1.0
     */
    public class Bean {

        private String beanIn;

        private String beanOut;

        private String packageName;

        private boolean inverted;

        /**
         * Constructort.
         */
        public Bean() {
        }

        /**
         * Is this a two-way translation?
         * 
         * @return true: yes, false: no.
         */
        public boolean isInverted() {
            return inverted;
        }

        /**
         * Is this a two-way translation?
         * 
         * @param inverted
         *            true: yes, false: no.
         */
        public void setInverted(boolean inverted) {
            this.inverted = inverted;
        }

        /**
         * Bean coming into the translator.
         * 
         * @param beanIn
         *            Bean coming into the translator.
         */
        public void setBeanIn(String beanIn) {
            this.beanIn = beanIn;
        }

        /**
         * Bean coming out of the translator.
         * 
         * @param beanOut
         *            Bean coming out of the translator.
         */
        public void setBeanOut(String beanOut) {
            this.beanOut = beanOut;
        }

        /**
         * The name of the package the translator is located in.
         * 
         * @param packageName
         *            The name of the package the translator is located in.
         */
        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        /**
         * Bean coming into the translator.
         * 
         * @return Bean coming into the translator.
         */

        public String getBeanIn() {
            return beanIn;
        }

        /**
         * Bean coming out of the translator.
         * 
         * @return Bean coming out of the translator.
         */

        public String getBeanOut() {
            return beanOut;
        }

        /**
         * The name of the package the translator is located in.
         * 
         * @return The name of the package the translator is located in.
         */
        public String getPackageName() {
            return packageName;
        }
    }
}
