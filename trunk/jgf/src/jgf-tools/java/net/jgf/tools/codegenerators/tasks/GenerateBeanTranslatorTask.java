package net.jgf.tools.codegenerators.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import net.jgf.tools.codegenerators.GenerateBeanTranslator;
import net.jgf.tools.codegenerators.GenerateBeanTranslator.Translator;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class GenerateBeanTranslatorTask extends Task {

    private String            destDir;

    private Vector < Bean >   beans       = new Vector < Bean >();
    
    private String mapPackage;

    @SuppressWarnings("unchecked")
    @Override
    public void execute() throws BuildException {
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
                Class beanInClass = GenerateBeanTranslatorTask.class.getClassLoader().loadClass(bean.getBeanIn());
                Class beanOutClass = GenerateBeanTranslatorTask.class.getClassLoader().loadClass(bean.getBeanOut());
                Translator translator = GenerateBeanTranslator.convert(beanInClass, beanOutClass, bean.getPackageName());
                storeTranslator(destDir, bean.getPackageName(), translator);
                writer.write("\t\ttranslatorsByKey.put(\"" + beanInClass.getName() + "\", new " + bean.getPackageName() + "." + translator.getTranslatorName() + "());\n");
                if (bean.isInverted()) {
                    translator = GenerateBeanTranslator.convert(beanOutClass, beanInClass, bean.getPackageName());
                    storeTranslator(destDir, bean.getPackageName(), translator);
                    writer.write("\t\ttranslatorsByKey.put(\"" + beanOutClass.getName() + "\", new " + bean.getPackageName() + "." + translator.getTranslatorName() + "());\n");
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
        writer.write("package " + mapPackage);
        writer.write(";\n\nimport java.util.LinkedHashMap");
        writer.write(";\nimport net.jgf.translators.Translator");
        writer.write(";\n\npublic final class TranslatorMap {\n\n");
        writer.write("\tprivate static final LinkedHashMap <String, Translator> translatorsByKey = new LinkedHashMap <String, Translator>();\n\n");
        writer.write("\tprivate TranslatorMap() {\n");
    }
    
    private void writeTranslatorMapFooter(FileWriter writer) throws IOException {
        writer.write("\t}\n\n");
        writer.write("\tpublic static Object translate(Object beanToTranslate) {\n");
        writer.write("\t\treturn translatorsByKey.get(beanToTranslate.getClass().getName()).translate(beanToTranslate);\n");
        writer.write("\t}\n}");
    }

    private void storeTranslator(String destDir, String packageName, Translator translator) throws IOException {
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

    // The setter for the "destDir" attribute
    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }
    
    public void setMapPackage(String mapPackage) {
        this.mapPackage = mapPackage;
    }

    public Bean createBean() {
        Bean bean = new Bean();
        beans.add(bean);
        return bean;
    }

    public class Bean {

        private String  beanIn;

        private String  beanOut;

        private String  packageName;

        private boolean inverted;

        public boolean isInverted() {
            return inverted;
        }

        public void setInverted(boolean inverted) {
            this.inverted = inverted;
        }

        public Bean() {
        }

        public void setBeanIn(String beanIn) {
            this.beanIn = beanIn;
        }

        public void setBeanOut(String beanOut) {
            this.beanOut = beanOut;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getBeanIn() {
            return beanIn;
        }

        public String getBeanOut() {
            return beanOut;
        }

        public String getPackageName() {
            return packageName;
        }
    }
}
