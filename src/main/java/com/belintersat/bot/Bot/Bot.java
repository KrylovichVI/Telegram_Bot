package com.belintersat.bot.Bot;

import com.belintersat.bot.Parser.Lists.HappyBirthdayList;
import com.belintersat.bot.ParserXLS.Parser;
import com.belintersat.bot.Weather.Weather;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.methods.send.SendVideo;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;




public class Bot extends TelegramLongPollingBot{

    private static final Logger logger = Logger.getLogger(Bot.class.getName());

    private long CHAT_TEST_ID = -1001135491699L;
    private long CHAT_ZAVOD_ID = -250523908L;
    private long CHAT_NKU_ID = -1001489666731L;
    private long CHAT_GUS_ID = -1001074603910L;

    private String pathNameOds = "ods/2019_Orbit_Control_Plan.ods";

    private final String[] urlPhoto = {
            "http://belintersat.by/images/Telegrambot/happy_summer.jpg",
            "http://belintersat.by/images/Telegrambot/happy_autumn.jpg",
            "http://belintersat.by/images/Telegrambot/happy_spring.jpg",
            "http://belintersat.by/images/Telegrambot/happy_winter.jpg",
            "http://belintersat.by/images/Telegrambot/SpongeBot.mp4",
            "http://belintersat.by/images/23-feb.jpg",
            "http://belintersat.by/images/8-marta.jpg",
            "http://en.belintersat.by/images/16-jan.jpg",
            "https://belintersat.com/images/gallery/zapusk_foto/belintersat_satellite.jpg"};

    private LinkedHashMap<String, ArrayList<String>> linkedHashMap;


    @Override
    public void onUpdateReceived(Update update){
       // System.out.println(update.getMessage().getFrom().getFirstName() + ": " + update.getMessage().getText());
        Message message = update.getMessage();

        if ((message != null) && (message.hasText())){
            System.out.println(message.getChatId());
            System.out.println(message.getSticker());
            try {
                sendMsg(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername(){
        return "Sponge Bot";
    }

    @Override
    public String getBotToken(){
        return "443780992:AAEzMQA70F42iiX3oFjxJ0beFecLo_ialz8";
    }

    private void sendMsg(Message message) throws IOException {
        if ((message.getText().equalsIgnoreCase("что со спутником")) || (message.getText().equalsIgnoreCase("что со спутником?")) ||
          (message.getText().equalsIgnoreCase("что со спутником ?")))
            sendMsgSatellite(message);
         else if (message.getText().startsWith("#") && !message.getText().contains("#list") && !message.getText().contains("#погода"))
            sendMsgBelintersatList(message);
        else if(message.getText().equalsIgnoreCase("Кто проживает на дне океана?") || message.getText().equalsIgnoreCase("Кто проживает на дне океана") ||
                message.getText().equalsIgnoreCase("Кто проживает на дне океана ?"))
            sendMsgBotName(message);
        else if(message.getText().equalsIgnoreCase("#list")){
            sendAbonentsList(message);
        }
        else if((message.getText().contains("sent to:") || message.getText().contains("Sent to:"))
                ||(message.getText().contains("prof to:") || message.getText().contains("Prof to:"))){
            sendBotMsg(message);
        }
        else if(message.getText().contains("совещание в") || message.getText().contains("Совещание в")){
            sendMsgMeeting(message);
        }
        else if(message.getText().equalsIgnoreCase("#погода")){
            sendMsgWeather(message);
        }
        else if(message.getText().equalsIgnoreCase("/help")) {
            sendMsgHelper(message);
        }
//        else if(message.getText().equalsIgnoreCase("/спутник")) {
//            sendMsgGUS(Calendar.getInstance());
//        }
        else if(message.getText().equalsIgnoreCase("/plan")){
            sendMsgGusFile(message);
        }
    }


    private void sendMsgBelintersatList(Message message) {
        ClassLoader cl = this.getClass().getClassLoader();
        HashMap<String, String> belintersatMap = Parser.parseAbonentList(ClassLoader.getSystemResourceAsStream("xls/List_Employer.xls")).getBelintersatMap();
        Set<String> keySet = Parser.parseAbonentList(ClassLoader.getSystemResourceAsStream("xls/List_Employer.xls")).getBelintersatMap().keySet();
        SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
        for (String key : keySet) {
            if (message.getText().equalsIgnoreCase(key)){
            sendMessage.setText(belintersatMap.get(key));
            break;
            }
        }
        if(sendMessage.getText() == null){
            sendMessage.setText("Такой сотрудник не работает на НКУ!");
        }
        try {
            sendMessage(sendMessage);
            logger.info("Команда " + message.getText() +  " выполнена ");
        } catch (TelegramApiException e) {
            String ex = Throwables.getStackTraceAsString(e);
            e.printStackTrace();
            logger.error("sendMessage" + message.getText() + " не выполнена " + ex);
        }
    }
  
    public void sendMsgBirthday(Date date){
        HappyBirthdayList happyBirthdayList = Parser.parserBirthday(ClassLoader.getSystemResourceAsStream("xls/ListBirthday.xls"));
        ArrayList<String> list = happyBirthdayList.getUsers();
        String result = "";
        SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);

        if (list.size() == 0)
            return;
        for (int i = 0; i < list.size(); i++) {
            if (list.size() == 1) {
                result = list.get(i) + ". ";
            } else if (list.size() > 1) {
                if (i < list.size()) {
                     if (i == list.size() - 1)
                         result = result + list.get(i) + ". ";
                    else
                         result = result + list.get(i) + ", ";
                }
            }
        }
        sendMessage.setText("  Сегодня День Рождения у " + result + "Поздравляю Вас с Днем Рождения! " +
        "От всей души желаю чтоб в жизни всегда было много радостных событий и ярких впечатлений, новых перспектив и начинаний, увлекательных путешествий и интересных встреч. " +
                "Пусть удача и успех сопутствуют во всем! " +
                "Sponge Bot и команда Belintersat");
        SendPhoto sendPhoto = new SendPhoto()
                                    .setChatId(CHAT_NKU_ID);

        if(date.getMonth() == Calendar.JUNE || date.getMonth() == Calendar.JULY || date.getMonth() == Calendar.AUGUST ){
            sendPhoto.setPhoto(urlPhoto[0]);
        } else if(date.getMonth() == Calendar.SEPTEMBER || date.getMonth() == Calendar.OCTOBER || date.getMonth() == Calendar.NOVEMBER ){
            sendPhoto.setPhoto(urlPhoto[1]);
        } else if(date.getMonth() == Calendar.MARCH || date.getMonth() == Calendar.APRIL || date.getMonth() == Calendar.MAY ) {
            sendPhoto.setPhoto(urlPhoto[2]);
        }  else if(date.getMonth() == Calendar.DECEMBER || date.getMonth() == Calendar.JANUARY || date.getMonth() == Calendar.FEBRUARY ) {
            sendPhoto.setPhoto(urlPhoto[3]);
        }

        try{
            sendMessage(sendMessage);
            sendPhoto(sendPhoto);

            sendMessage.setChatId(CHAT_ZAVOD_ID);
            sendPhoto.setChatId(CHAT_ZAVOD_ID);

            sendMessage(sendMessage);
            sendPhoto(sendPhoto);
            logger.info("Поздравление С Днем Рождения произошло успешно ");
        }
        catch(TelegramApiException e) {
            String ex = Throwables.getStackTraceAsString(e);
            e.printStackTrace();
            logger.error("Поздравление С Днем Рождения не отправлено " + ex);
        }
    }
  
        private void sendMsgBotName(Message message){
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            sendMessage.setText(getBotUsername());
            try{
                sendMessage(sendMessage);
                logger.info("Получено имя бота ");
            }catch(TelegramApiException e){
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Имя бота не получено " + ex);
            }
        }

        private void sendBotMsg(Message message){
            SendMessage sendMessage = new SendMessage();
            String text = message.getText();

            if((text.substring(0, 8).contains("sent to:")) ||(text.substring(0, 8).contains("Sent to:"))){
                sendMessage.setChatId(CHAT_NKU_ID);
            } else if((text.substring(0, 8).contains("prof to:")) ||(text.substring(0, 8).contains("Prof to:"))){
                sendMessage.setChatId(CHAT_ZAVOD_ID);
            } else {
                logger.error("Не верно введена команда Sent to: ");
                return;}

            String result = text.substring(8, text.length());
            sendMessage.setText(result);
            try {
                sendMessage(sendMessage);
                logger.info("Команда Sent to: " + message.getFrom().getFirstName() + " выполнена успешно" );
            } catch (TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Команда Sent to:  не выполнена " + ex);
            }
        }

        public void sendMsgChristmas(String text){
            SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);
            SendVideo sendVideo = new SendVideo().setChatId(CHAT_NKU_ID);

            sendMessage.setText(text);

                sendVideo.setVideo(urlPhoto[4]);
            try{
                sendMessage(sendMessage);
                sendVideo(sendVideo);
                logger.info("Поздравление с рождеством отправлено ");

             }
            catch(TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Рождественское поздравление не отправлено " + ex);
            }
        }

        public void sendMsg23thFebruary(){

            SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);
            SendPhoto sendPhoto = new SendPhoto()
                    .setChatId(CHAT_NKU_ID);
            sendPhoto.setPhoto(urlPhoto[5]);
            sendMessage.setText(" С праздником мужества, славы и силы! \nЧествуем вас, дорогие мужчины! \nПусть будет смех и радость в нем," +
                    "\nЧтоб богатырским было здоровье.\n \nЯсного неба, лишь мирных сражений, \nРоста карьерного и достижений.\n" +
                    "Пусть на все блага жизнь будет щедра. \nРадости, счастья, любви вам, добра.\n" +
                    "\nSponge Bot и команда Belintersat");
            try{
                sendMessage(sendMessage);
                sendPhoto(sendPhoto);
                logger.info("Поздравление с 23-февраля отправлено ");
            }
            catch(TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Поздравление на 23-февраля не отправлено " + ex);
            }
        }

        public void sendMsg8thMarch(){
            SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);
            SendPhoto sendPhoto = new SendPhoto()
                    .setChatId(CHAT_NKU_ID);
            sendPhoto.setPhoto(urlPhoto[6]);
            sendMessage.setText(" С Международным женским днем! \nПусть будет много счастья в нём, \nИ красоты, сюрпризов ярких," +
                    "\nИ комплиментов самых сладких.\n \nПусть сердце верит, любит, ждет, \nИ счастье в светлый дом придет.\n" +
                    "По пустякам — не огорчаться, \nА жизнью, в целом, наслаждаться.\n" +
                    "\nSponge Bot и команда Belintersat");
            try{
                sendMessage(sendMessage);
                sendPhoto(sendPhoto);
                logger.info("Поздравление с 8-марта отправлено ");
            }
            catch(TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Поздравление на 8-марта не отправлено " + ex);
            }
        }

        private void sendMsgSatellite(Message message){
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            sendMessage.setText("Сегодня все ок, " + getTimes() + " сутки полета!");
            //SendSticker sendSticker = new SendSticker().setChatId(message.getChatId());
            //sendSticker.setSticker(STICKER);

            try{
                sendMessage(sendMessage);
                //sendSticker(sendSticker);
                logger.info("Команда что со спутником выполнена успешно ");
            }catch(TelegramApiException e){
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Команда что со спутником не отправлена " + ex);
            }
        }

        public void sendMsgNotification(){
            if(!isGetTimes()) return;
            SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);
            sendMessage.setText("Господа, у нас сегодня " + getTimes() + " сутки полета! УРА!!!");

            try {
                sendMessage(sendMessage);
                logger.info("Круглая дата по сутком полета выполнено успешно ");
            } catch (TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Круглая дата по сутком полета не отправлена " + ex);
            }
        }

        private void sendAbonentsList(Message message){

            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            HashMap<String, String> belintersatList = Parser.parseSipAbonents(ClassLoader.getSystemResourceAsStream("xls/IPTelephone_Abonents.xls")).getBelintersatMap();
            Set<String> keySet = belintersatList.keySet();
            String result = "";
            for(String key: keySet){
                result += key + " - " +  belintersatList.get(key) + "\n";
            }
            sendMessage.setText(result);
            try {
                sendMessage(sendMessage);
                logger.info("Команда #list выполнена успешно ");
            } catch (TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Команда #list не выполнена " + ex);
            }

        }

        private void sendMsgMeeting(Message message) {
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            sendMessage.setText("Меня не будет.");
            try {
                sendMessage(sendMessage);
                logger.info("Команда совещание выполнена успешно ");
            } catch (TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Команда совещание не выполнена " + ex);
            }
        }

        private void sendMsgWeather(Message message) {
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            sendMessage.setText("");

            sendMessage.setText(Weather.showWeather());
            try {
                sendMessage(sendMessage);
                logger.info("Команда #погода выполнена успешно ");
            } catch (TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Команда #погода не выполнена " + ex);
            }
        }

        public void sendMsgGUS(Calendar calendar){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            String format = simpleDateFormat.format(calendar.getTime());

            linkedHashMap = Parser.readODS(new File(ClassLoader.getSystemResource(pathNameOds).getPath()), calendar).getLinkedMap();

            ArrayList<String> data = linkedHashMap.get(format);

            String result = "";
            Boolean[] position = new Boolean[data.size()];
            int count = 0;
            for(int i = 0; i < data.size(); i++){
                if(!data.get(i).isEmpty()){
                    count++;
                    position[i] = true;
                } else {
                    position[i] = false;
                }
            }

            if(count > 2) {
                SendMessage sendMessage = new SendMessage().setChatId(CHAT_GUS_ID);

                ArrayList<String> text = linkedHashMap.get("Date");
                for (int i = 0; i < text.size(); i++) {
                    if (i == 0) {
                        result += "На ближайшие сутки \"" + data.get(i) + "\" запланировано: \n";
                        continue;
                    } else if(i == 1){
                        continue;
                    }
                    if(position[i]) {
                        result += text.get(i) + ": " + (data.get(i).isEmpty() ? "-" : data.get(i)) + (i == 3 ? " UTC" : "") + "\n";
                    }                 }


                sendMessage.setText(result);
                try {
                    sendMessage(sendMessage);
                    logger.info("Gus уведомление выполнена успешно ");
                } catch (TelegramApiException e) {
                    String ex = Throwables.getStackTraceAsString(e);
                    e.printStackTrace();
                    logger.error("Gus уведомление по утрам не выполнена " + ex);
                }
            }
            logger.info("Нет запланированных моневров.");
        }

        public void sendMsgGusFile(Message message) throws IOException {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            String format = simpleDateFormat.format(calendar.getTime());

            SendDocument sendDocument = new SendDocument().setChatId(message.getChatId());


            File gusFile = getGusFile(calendar, format);
            if(gusFile.isFile()) {
                sendDocument.setNewDocument(gusFile);

                try {
                    sendDocument(sendDocument);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else{
                SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
                sendMessage.setText("Извините, мы не можем сгенерировать вам файл");
                try {
                    sendMessage(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

    private File getGusFile(Calendar calendar, String format) throws IOException {
        if(linkedHashMap == null)
            linkedHashMap = Parser.readODS(new File(ClassLoader.getSystemResource(pathNameOds).getPath()), calendar).getLinkedMap();

        if(linkedHashMap.containsKey(format)) {

            LinkedHashMap<String, ArrayList<String>> sortedLinkedHashMap = getIndexLinkedHashMap(linkedHashMap, format);

//           linkedMap.entrySet()
//                    .stream()
//                    .skip(index)
//                    .collect(Collectors.toMap(
//                            key -> key,
//                            key -> (linkedMap.get(key)),
//                            (e1, e2) -> e1, LinkedHashMap::new
//                    ));

            return Parser.writeODS(sortedLinkedHashMap);
        }
        return null;
    }

    public void sendMsgWeather() {
            SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);
            sendMessage.setText(Weather.showWeather());
            try {
                sendMessage(sendMessage);
                logger.info("Утреняя #погода выполнена успешно ");
            } catch (TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Уренняя команда погоды по утрам не выполнена " + ex);
            }
        }

        public void sendMsgBelintersat(){
            String str = null;
            int result = getTimes()/365;
            SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);
            SendPhoto sendPhoto = new SendPhoto()
                    .setChatId(CHAT_NKU_ID);
            sendPhoto.setPhoto(urlPhoto[7]);
            if(result < 5 ) {
                str = " года";
            } else {
                str = " лет";
            }

            sendMessage.setText("Сегодня " + result + str + " cо дня запуска. Поздравляю!!!" +
                    "\nSponge Bot и команда Belintersat");
            try{
                sendMessage(sendMessage);
                logger.info("Команда 15 января выполнена успешно ");
            }
            catch(TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Команда 15 января не выполнена " + ex);
            }

        }

        public void sendMsgApril_12() {
            SendMessage sendMessage = new SendMessage().setChatId(CHAT_NKU_ID);
            SendPhoto sendPhoto = new SendPhoto()
                    .setChatId(CHAT_NKU_ID);
            sendPhoto.setPhoto(urlPhoto[8]);
            sendMessage.setText("12 апреля Всемирный день авиации и космонавтики." +
                    "\nЭтот день является одним из самых  " +
                    "\nзначимых для всех, кто так или иначе связан с " +
                    "\nкосмической отраслью, кто расширяет горизонты и " +
                    "\nоткрывает новые перспективы для развития всего" +
                    "\nчеловечества. Поздравляем всех с этим праздником." +
                    "\nМы надеемся, что наше сотрудничество и в" +
                    "\nдальнейшем будет плодотворным.  Спасибо вам за то, что вы с нами!" +
                    "\nSponge Bot и команда Belintersat");
            try{
                sendMessage(sendMessage);
                sendPhoto(sendPhoto);
                logger.info("Поздравление с 12-апреля отправлено ");
            }
            catch(TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Поздравление на 12-апреля не отправлено " + ex);
            }
        }

        public void sendMsgHelper(Message message){
            SendMessage sendMessage = new SendMessage().setChatId(message.getChatId());
            HashMap<String, String> belintersatList = Parser.parseSipAbonents(ClassLoader.getSystemResourceAsStream("xls/help.xls")).getBelintersatMap();
            Set<String> keySet = belintersatList.keySet();
            String result = "";
            for(String key: keySet){
                result += key + " - " +  belintersatList.get(key) + "\n";
            }
            sendMessage.setText(result);
            try {
                sendMessage(sendMessage);
                logger.info("Команда /help выполнена успешно ");
            } catch (TelegramApiException e) {
                String ex = Throwables.getStackTraceAsString(e);
                e.printStackTrace();
                logger.error("Команда /help не выполнена " + ex);
            }
        }

        private  LinkedHashMap<String, ArrayList<String>> getIndexLinkedHashMap(LinkedHashMap<String, ArrayList<String>> linkedMap, String format) {
            int count = 0;
            Iterator<String> iterator = linkedMap.keySet().iterator();
            LinkedHashMap<String, ArrayList<String>> mapIndex = new LinkedHashMap<>();

            while (iterator.hasNext()){
                String next = iterator.next();
                if(next.equals("Date")){
                    mapIndex.put(next, linkedMap.get(next));
                }
                if(next.equals(format)){
                    count++;
                }
                if(count == 1){
                    mapIndex.put(next, linkedMap.get(next));
                }

            }
            return mapIndex;
        }

        private int getTimes() {
            GregorianCalendar calendar = new GregorianCalendar(2016, Calendar.JANUARY, 15);
            GregorianCalendar calendar1 = new GregorianCalendar();
            long different = calendar1.getTimeInMillis() - calendar.getTimeInMillis();
            return (int)(different / (1000*60*60*24));
        }

        private boolean isGetTimes(){
            int result = getTimes();
            if(result <= 999){
                int a, b, c;
                a =  result / 100;
                b = (result / 10) % 10;
                c =  result % 10;
                return ( (b == c && c == 0) || ( a == b && b == c))? true : false;
            } else if(result >= 1000 && result < 10000){
                int a, b, c, d;
                a =  result / 1000;
                b = (result / 100) % 10;
                c = (result / 10) % 10;
                d =  result % 10;
                return  ((b == c && c == d && d == 0) || (a == b && b == c && c == d)) ?  true :  false ;
            }
            return false;
        }

}
