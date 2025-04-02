package cn.chengzhiya.visitor;

import cn.chengzhiya.util.HttpUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DbHttpUtilVisitor extends ClassVisitor {
    public DbHttpUtilVisitor(ClassWriter classWriter) {
        super(Opcodes.ASM9, classWriter);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        if (descriptor.startsWith("(L") && descriptor.contains("handy") && descriptor.contains("lib/expand/PluginDbReq") && descriptor.endsWith(";)Ljava/lang/String;")) {
            System.out.println("开始获取SQL指令!");
            return new MethodVisitor(Opcodes.ASM9, mv) {
                @Override
                public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                    // 修改获取SQL的服务器地址
                    if (owner.contains("lib/DbHttpUtil") && "DB_URL".equals(name) && "Ljava/lang/String;".equals(descriptor)) {
                        if (opcode == Opcodes.GETSTATIC) {
                            System.out.println("已强制修改 " + owner + ".DB_URL 的返回值为" + HttpUtil.getNewURL());
                            super.visitLdcInsn(HttpUtil.getNewURL());

                            return;
                        }
                    }

                    // 欺骗插件已验证 以从云端获取SQL
                    if (owner.contains("lib/SignConstants") && "COMMAND_PERMISSION".equals(name) && "Z".equals(descriptor)) {
                        if (opcode == Opcodes.GETSTATIC) {
                            System.out.println("已强制修改 " + owner + ".COMMAND_PERMISSION 的返回值为false");
                            super.visitInsn(Opcodes.ICONST_0);

                            return;
                        }
                    }
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                }

                @Override
                public void visitInsn(int opcode) {
                    // 打印执行的SQL
                    if (opcode == Opcodes.ARETURN) {
                        super.visitInsn(Opcodes.DUP);
                        super.visitFieldInsn(Opcodes.GETSTATIC,
                                "java/lang/System",
                                "out",
                                "Ljava/io/PrintStream;");
                        super.visitInsn(Opcodes.SWAP);
                        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                                "java/io/PrintStream",
                                "println",
                                "(Ljava/lang/String;)V",
                                false);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return mv;
    }
}
