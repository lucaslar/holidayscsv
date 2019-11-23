import de.jollyday.HolidayCalendar;
import de.jollyday.HolidayManager;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    private final static HolidayManager MANAGER = HolidayManager.getInstance(HolidayCalendar.GERMANY);

    private final static SimpleDateFormat SD_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat SD_DAY = new SimpleDateFormat("d");
    private final static SimpleDateFormat SD_MONTH = new SimpleDateFormat("M");
    private final static SimpleDateFormat SD_YEAR = new SimpleDateFormat("yyyy");

    private static GregorianCalendar date =
            /*
            SELECT MIN(mindate)
            FROM (
                    SELECT MIN(Bestelldatum) AS mindate FROM Data_HAM.dbo.Lieferauftrag
                    UNION
                    SELECT MIN(Lieferdatum) AS mindate FROM Data_HAM.dbo.Lieferauftrag
                    UNION
                    SELECT MIN(Bestelldatum) AS mindate FROM Data_FRA.dbo.Lieferung

            ) AS dates
             */
            new GregorianCalendar(2016, Calendar.JANUARY, 1);

    public static void main(String[] args) {
        final GregorianCalendar today = new GregorianCalendar();

        try {
            final FileWriter csvWriter = new FileWriter("dates.csv");
            csvWriter.append("Datum,Tag,Monat,Jahr,Quartal,IstFeiertag\n");

            while (date.before(today)) {
                csvWriter.append(String.join(",", getDateValues(date))).append("\n");
                date.add(Calendar.DAY_OF_MONTH, 1);
            }
            csvWriter.flush();
            csvWriter.close();
            System.out.println("Your date file has been generated successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getDateValues(GregorianCalendar date) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(SD_FORMAT.format(date.getTime()));
        list.add(SD_DAY.format(date.getTime()));
        list.add(SD_MONTH.format(date.getTime()));
        list.add(SD_YEAR.format(date.getTime()));
        list.add("" + (1 + (date.get(Calendar.MONTH)) / 3));
        list.add(MANAGER.isHoliday(date) ? "1" : "0");
        return list;
    }
}
