

import classloaders.MyClassLoader;
import exceptions.CompilationException;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        StringBuilder code = new StringBuilder();
        String line = "line";

        code.append("public class SomeClass {\n");
        code.append("public void doWork(){\n");

        Scanner sc = new Scanner(System.in);
        while(!line.equals("")){
            line = sc.nextLine();
            code.append(line).append("\n");
        }


        File dirDynamic = new File("dynamic");
        dirDynamic.mkdir();
        File ourClass = new File("dynamic","SomeClass.java");
        ourClass.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(ourClass));

        code.append("}" + "\n" + "}");
        writer.write(code.toString());

        writer.flush();



        //Определяем путь до нашего класса
        String classFilePath = ourClass.getAbsolutePath().substring(0, ourClass.getAbsolutePath().length()-4)+"class";
        File classFile = new File(classFilePath);
        if(classFile.delete()){
            System.out.println("Предыдущий файл класса удален");
        }

        Properties p = System.getProperties();

        String separator = p.getProperty("file.separator");

        String jrePath = p.getProperty("java.home");

        int lastIndex = jrePath.lastIndexOf(separator);
        String javac = jrePath + separator + "bin" + separator + "javac";

        if(p.getProperty("sun.desktop").equals("windows"))
            javac+=".exe";
        else javac+=".sh";

        File jc = new File(javac);
        if(!jc.isFile())
            throw new FileNotFoundException("Компилятор по адресу "+ jc.getAbsolutePath() +" недоступен ");

        Process process = Runtime.getRuntime().exec(javac + " " + ourClass.getAbsolutePath());
        try {
        if(process.waitFor()!=0){
         throw new CompilationException();
        }
            }
            catch (CompilationException e){
                System.out.println("Произошла ошибка компиляции");
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }


        ClassLoader loader = new MyClassLoader();
        Class myClass = loader.loadClass(classFilePath);

        Method doWork = myClass.getMethod("doWork");
        doWork.invoke(myClass.newInstance());
        sc.close();
        writer.close();

    }
}


