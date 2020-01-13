package io.johnsonlee.booster.benchmark

import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.didiglobal.booster.transform.asm.findAll
import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode

class AsmThreadTransformer : ClassTransformer {

    override fun transform(context: TransformContext, klass: ClassNode): ClassNode {
        klass.methods.forEach { method ->
            method.instructions.findAll(Opcodes.INVOKEVIRTUAL).map {
                it as MethodInsnNode
            }.filter {
                it.name == "setName" && context.klassPool["java/lang/Thread"].isAssignableFrom(it.owner)
            }.forEach { invoke ->
                method.instructions.insertBefore(invoke, LdcInsnNode("####"))
                method.instructions.insertBefore(invoke, MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false))
            }
        }
        return klass
    }

}