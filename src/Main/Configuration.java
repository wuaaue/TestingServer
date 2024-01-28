package Main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Configuration {//класс для работы с настройками сервера
    static  int port;
    static final String PROPERTIS_FILE="./server.ini";
    static String BASE_DIRECTORY;
    static {
        Properties properties = new Properties();//Properties позволяет хранить пары "имя свойства - значение свойства"
        try(FileInputStream fileInputStream= new FileInputStream(PROPERTIS_FILE)){
            properties.load(fileInputStream);//загрузка свойства из файла
            port =Integer.parseInt(properties.getProperty("PORT"));
            BASE_DIRECTORY =properties.getProperty("BASE_DIRECTORY");
        }catch (FileNotFoundException e){
            System.out.println("Файл свойств не найден: "+e);
        }catch (IOException e){
            System.out.println("Ошибка чтения файла: "+e);
        }
    }
}
