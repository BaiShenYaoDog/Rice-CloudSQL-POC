package cn.chengzhiya.transformer.title;

import cn.chengzhiya.visitor.SignUtilVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class SignUtilTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className.contains("handy") && className.contains("lib/SignUtil")) {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            cr.accept(new SignUtilVisitor(cw), ClassReader.EXPAND_FRAMES);
            return cw.toByteArray();
        }
        return classfileBuffer;
    }
}
