import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory


public class MyExtension {
    String message
    Boolean isDebug
    final DefaultConfig defaultConfig

    @javax.inject.Inject
    public MyExtension(ObjectFactory objectFactory) {
        // 创建一个 DefaultConfig 对象
        defaultConfig = objectFactory.newInstance(DefaultConfig)
    }

    // 必须要实现 defaultConfig 方法
    void defaultConfig(Action<? super DefaultConfig> action) {
        action.execute(defaultConfig)
    }

    String getMessage() {
        return message
    }
    void setMessage(String message) {
        println "set message = "+message
        this.message = message
    }
}

public class DefaultConfig {
    String applicationId
    int minSdkVersion
    int targetSdkVersion
}

public class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('pluginTest') {
            doLast {
                println 'Hello World'
            }
        }

        // 通过参数传递把 ObjectFactory 传递给构造函数
        project.extensions.create('myExtension', MyExtension.class, project.objects)
        // 实现一个名称为testPlugin的task，设置分组为 myPlugin，并设置描述信息
        project.task('testPlugin', group: "myPlugin", description: "This is my test plugin") {
            doLast {
                println "**## Test This is my first gradle plugin in testPlugin task **"

                println project.myExtension.message
                println project.myExtension.isDebug

                println project.myExtension.defaultConfig.applicationId
                println project.myExtension.defaultConfig.minSdkVersion
                println project.myExtension.defaultConfig.targetSdkVersion
            }

            //println project.myExtension.buildConfigs.size()
        }

    }
}
