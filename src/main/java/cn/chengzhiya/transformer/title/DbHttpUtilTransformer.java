package cn.chengzhiya.transformer.title;

import cn.chengzhiya.visitor.DbHttpUtilVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class DbHttpUtilTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className.contains("handy") && className.contains("lib/DbHttpUtil")) {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            cr.accept(new DbHttpUtilVisitor(cw), ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }
        return classfileBuffer;
    }
}
