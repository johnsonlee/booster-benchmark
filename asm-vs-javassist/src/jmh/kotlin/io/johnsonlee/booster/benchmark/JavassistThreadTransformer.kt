package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.javassist.ClassTransformer
import javassist.CtClass
import javassist.bytecode.analysis.Type
import javassist.expr.ExprEditor
import javassist.expr.MethodCall

const val dollar = '$'

class JavassistThreadTransformer : ClassTransformer {

    override fun transform(context: TransformContext, klass: CtClass): CtClass {
        val klassThread = Type.get(klass.classPool["java.lang.Thread"])
        klass.methods.forEach { method ->
            method.instrument(object: ExprEditor() {
                override fun edit(invoke: MethodCall) {
                    if ("${invoke.methodName}${invoke.signature}" == "setName(Ljava/lang/String;)V" && klassThread.isAssignableFrom(Type.get(klass.classPool.get(invoke.className)))) {
                        invoke.replace("""{
                            $0.setName("####".concat($1));                           
                        }""".trimIndent())
                    }
                }
            })
        }
        return klass
    }

}