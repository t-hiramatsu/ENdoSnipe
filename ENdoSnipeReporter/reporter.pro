-injars dist\endosnipe-reporter.jar
-outjars dist\endosnipe-reporter.obfuscate.jar

-libraryjars '<java.home>\lib\rt.jar'
-libraryjars 'lib\dependency'(org/eclipse/jface/**,org/eclipse/ui/**,org/eclipse/swt/**,org/eclipse/core/commands/**,org/eclipse/core/runtime/**,org/eclipse/jdt/ui/**,org/osgi/framework/**,org/eclipse/jface/**,org/eclipse/ui/**,org/eclipse/swt/**,org/eclipse/core/commands/**,org/eclipse/core/runtime/**,org/eclipse/jdt/ui/**,org/osgi/framework/**)
-libraryjars 'lib'(!dependency/**.class,**.class)

-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

-verbose
-dontusemixedcaseclassnames

#-keepattributes *
-keepattributes Signature,RuntimeVisibleAnnotations,InnerClasses

# Keep - Applications. Keep all application classes that have a main method.
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}


-keep class jp.co.acroquest.endosnipe.report.ReporterPlugin {
    *;
}

-keep class jp.co.acroquest.endosnipe.report.ReporterPluginProvider {
    *;
}

-keep class jp.co.acroquest.endosnipe.report.controller.ReportPublishTask {
    *;
}

-keep class jp.co.acroquest.endosnipe.report.controller.ReportPublisher {
    *;
}

-keep class jp.co.acroquest.endosnipe.report.controller.ReportSearchCondition {
    *;
}

-keep class jp.co.acroquest.endosnipe.report.controller.ReportType {
    *;
}

-keep class jp.co.acroquest.endosnipe.report.entity.* {
    *;
}

-keep public class * implements jp.co.acroquest.endosnipe.report.controller.dispatcher.ReportPublishProcessor {
    public <methods>;
}
