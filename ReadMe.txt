对于 App 的 init 通常情况都是在 application 的 oncreate 方法里面。
高级点，通过 ContentProvider 可以自动创建，从而获取 Context。
对于组件化/模块化：
如果在每个 module 都使用 ContentProvider ，会造成 ContentProvider  漫天飞。
如果使用 common 的 ContentProvider 又会造成 Common 对 module 依赖，
或者 Common 过重，不适合所有项目。
本示例的目标是使用一个 ContentProvider ，每个 module 根据需要使用注解来 init 。
@AppInit

====================================================
使用示例：
1.添加依赖
    implementation project(path: ':lib-app-init')
    annotationProcessor project(':lib-app-init')
2.在 app 模块中，实现 IAppInit 类，需要 init 内容放在 init 方法中即可

3.android.defaultConfig 下添加配置，保证唯一类名，不是强制
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += [INIT_MODULE_NAME: project.getName()]
            }
        }

====================================================
代码说明：
lib-app-init 
- 这是一个 Android module，引用 apt-app-init ，
- 主要功能就是一个 contentprovider 

apt-app-init,注解处理器，
- 包含两部分，注解（annotation）和编译器（compiler）
- 引用 Google 的 auto-service

每个 module 都会生成一个类，在同一个包下
最后通过反射，获取该包下所有的文件
批量执行

错误查看命令：
gradlew compileDebugSources --stacktrace -info


====================================================