package org.itzheng.appinit.compiler;

import com.google.auto.service.AutoService;

import org.itzheng.appinit.AutoConstant;
import org.itzheng.appinit.annotation.AppInit;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * 这个类会自动执行
 */
@AutoService(Processor.class)
public class AppInitProcessor extends AbstractProcessor {
    //处理Element的工具类
    private Elements mElementUtils;
    //生成文件的工具
    private Filer mFiler;
    //日志信息的输出
    private Messager mMessager;
    /**
     * module 名，用于类名拼接，保证唯一
     */
    private String targetModuleName = "";

    /**
     * 输出错误信息，一旦执行这句，将编译错误
     *
     * @param msg
     */
    public void loge(String msg) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, msg);
    }

    /**
     * 日志输出
     *
     * @param msg
     */
    public void logi(String msg) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

        Map<String, String> map = processingEnv.getOptions();
        Set<String> keys = map.keySet();

        for (String key : keys) {
            logi("key:" + key);
            if ("INIT_MODULE_NAME".equals(key)) {
                this.targetModuleName = map.get(key);
            }
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(AppInit.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 处理流程 ，如果返回 true，说明后续不用再处理，和拦截器类似
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logi("AppInitProcessor process hello I am chinese ");
        //获取 AppInit 注解的集合
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(AppInit.class);
        if (set != null && !set.isEmpty()) {
            printAnnotations(set);
            mkjava(set);
            //处理掉了
            return true;
        }
        return false;
    }

    /**
     * print all
     *
     * @param set
     */
    private void printAnnotations(Set<? extends Element> set) {
        for (Element element : set) {
            logi("printAnnotations: " + element.toString());
            AppInit annotation = element.getAnnotation(AppInit.class);
            //全类名，优先级
//            mClass.add(new InitClass(element.toString(), annotation.priority()));
        }
    }


    /**
     * 创建Java文件，将所有用到注解的类，
     * 符合规矩的全部集中到一起
     *
     * @param set
     */
    private void mkjava(Set<? extends Element> set) {
        AutoConstant constant = AutoConstant.getInstance();
        String packageName = constant.AUTO_CLASS_PACKAGE_NAME;
        String newClassName = constant.getNewClassName(targetModuleName);

        StringBuilder builder = new StringBuilder()
                .append("package " + packageName + ";\n\n")
                .append("import " + "org.itzheng.appinit.base.BaseAutoClass" + ";\n\n")
                .append("public class ")
                .append(newClassName)
                .append(" extends BaseAutoClass")
                // open class
                .append(" {\n\n");

        // open method
        builder.append("\tpublic void addAll() {\n");
        //
        for (Element element : set) {
            //将所有注解添加到集合中
            AppInit annotation = element.getAnnotation(AppInit.class);
            //全类名，优先级
//            mClass.add(new InitClass(element.toString(), annotation.priority()));
            String string = String.format("\t\tadd(\"%s\", %d);\n",
                    element.toString(), annotation.priority());
            builder.append(string);
        }

        // close method
        builder.append("\t}\n");

        // close class
        builder.append("}\n");


        try { // write the file
            JavaFileObject source = mFiler.createSourceFile(packageName + "." + newClassName);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
            logi(e.toString());
        } catch (Exception e) {
            logi(e.toString());
        }

    }

}