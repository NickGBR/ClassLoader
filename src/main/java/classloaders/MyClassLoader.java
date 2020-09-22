package classloaders;


import java.io.*;

public class MyClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String path) throws ClassNotFoundException {
        File classFile = new File(path);

        if(!classFile.isFile()){
            throw new ClassNotFoundException();
        }

        BufferedInputStream bs = null;

        try {
            bs = new BufferedInputStream(new FileInputStream(classFile));
            byte[] b = new byte[(int) classFile.length()];
            bs.read(b);
            Class newClass = defineClass("SomeClass",b,0,b.length);
            return newClass;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                bs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
