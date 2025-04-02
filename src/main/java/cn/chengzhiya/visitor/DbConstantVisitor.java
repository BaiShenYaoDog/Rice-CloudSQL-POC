package cn.chengzhiya.visitor;

import cn.chengzhiya.util.HttpUtil;
import lombok.Getter;
import org.objectweb.asm.*;

@Getter
public class DbConstantVisitor extends ClassVisitor {
    private final String className;

    public DbConstantVisitor(ClassWriter classWriter, String className) {
        super(Opcodes.ASM9, classWriter);
        this.className = className;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if (value instanceof String s) {
            if (s.contains("api/public/db/sql")) {
                System.out.println("已强制修改 " + getClassName() + "." + name + " 的返回值为" + HttpUtil.getNewURL());
                return super.visitField(access, name, descriptor, signature, HttpUtil.getNewURL());
            }
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        if (name.equals("<clinit>") && descriptor.equals("()V")) {
            return new MethodVisitor(Opcodes.ASM9, mv) {
                @Override
                public void visitLdcInsn(Object value) {
                    if (value instanceof String s) {
                        if (s.contains("api/public/db/sql")) {
                            System.out.println("已强制修改 " + getClassName() + ".<clinit> 中赋值的URL为" + HttpUtil.getNewURL());
                            super.visitLdcInsn(HttpUtil.getNewURL());
                            return;
                        }
                    }
                    super.visitLdcInsn(value);
                }
            };
        }

        return mv;
    }
}
