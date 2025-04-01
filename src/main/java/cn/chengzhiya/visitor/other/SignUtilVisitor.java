package cn.chengzhiya.visitor.other;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class SignUtilVisitor extends ClassVisitor {
    public SignUtilVisitor(ClassWriter classWriter) {
        super(Opcodes.ASM9, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        // 欺骗插件已验证 以从云端获取SQL
        if (("isGenuine".equals(name) || "checkSign".equals(name)) && "()Z".equals(descriptor)) {
            return new MethodVisitor(Opcodes.ASM9, mv) {
                @Override
                public void visitCode() {
                    mv.visitInsn(Opcodes.ICONST_1);
                    mv.visitInsn(Opcodes.IRETURN);

                    System.out.println("已强制复写 " + name + "方法 返回true");

                    super.visitCode();
                }
            };
        }
        return mv;
    }
}
