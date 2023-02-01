import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

class MonthlyReport {
    String month;
    String name_of_good_income;
    String name_of_good_expense;
    ArrayList<String[]> dates = new ArrayList<>();
    int expenses = 0;
    int incomes = 0;
    int difference;

    boolean flag = false;

    public void fill_date() {
        for (int i = 1; i < dates.size(); i++) {
            if (dates.get(i)[1].equals("TRUE")) {
                incomes += Integer.parseInt(dates.get(i)[2]) * Integer.parseInt(dates.get(i)[3].trim());
            } else {
                expenses += Integer.parseInt(dates.get(i)[2]) * Integer.parseInt(dates.get(i)[3].trim());
            }
        }
        difference = incomes - expenses;
    }

    public String profitable_and_not_profitable_products() {
        int max = 0;
        int min = 0;
        int tmp = 0;
        int tempos = 0;
        for (int i = 1; i < dates.size(); i++) {
            if (dates.get(i)[1].equals("TRUE")) {
                tmp = Integer.parseInt(dates.get(i)[2]) * Integer.parseInt(dates.get(i)[3].trim());
                if (max <= tmp) {
                    max = tmp;
                    name_of_good_income = dates.get(i)[0];
                }
            } else {
                tempos = Integer.parseInt(dates.get(i)[2]) * Integer.parseInt(dates.get(i)[3].trim());
                if (min <= tempos) {
                    min = tempos;
                    name_of_good_expense = dates.get(i)[0];
                }
            }
        }
        return "Самый прибыльный товар: " + name_of_good_income + " " + max + ";\n Самый затратный товар: " +
                " " + name_of_good_expense + " " + min;
    }
}

class YearlyReport {
    String[][] expenses_and_incomes = new String[3][2];

    String year;
    ArrayList<String[]> dates_2 = new ArrayList<>();
    boolean flag = false;

    public void fill_date() {
        for (int i = 0; i < dates_2.size() - 1; i++) {
            if ((dates_2.get(i + 1)[2].trim()).equals("TRUE")) {
                expenses_and_incomes[i][0] = dates_2.get(i + 1)[1];
                expenses_and_incomes[i][1] = "+";
            } else {
                expenses_and_incomes[i][0] = "-" + dates_2.get(i + 1)[1];
                expenses_and_incomes[i][1] = "-";
            }
        }
    }

}


public class Main {

    public static String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Извините, мы не смогли прочитать ваш файл. Есть повреждения");
            return null;
        }
    }

    public static void read_monthly_reports(ArrayList<MonthlyReport> a) {
        String tmp;
        for (int i = 1; i < 4; i++) {
            tmp = readFileContentsOrNull("m.20210" + i + ".csv");
            // ArrayList<String[]> lines = new ArrayList<>();
            assert tmp != null;
            // System.out.println(Arrays.toString(lines.get(0)));
            a.add(new MonthlyReport());
            a.get(i - 1).flag = true;
            a.get(i - 1).month = "2021.0" + i;
            for (String date : tmp.split("\\n")
            ) {
                a.get(i - 1).dates.add(date.split(";"));
            }
        }
    }

    public static void read_year_reports(ArrayList<YearlyReport> a) {
        String tmp;

        tmp = readFileContentsOrNull("y.2021.csv");
        assert tmp != null;
        a.add(new YearlyReport());
        a.get(0).year = "2021";
        a.get(0).flag = true;
        for (String date : tmp.split("\\n")
        ) {
            a.get(0).dates_2.add(date.split(";"));
        }

    }

    public static void check_reports(ArrayList<MonthlyReport> a, ArrayList<YearlyReport> b) {
        b.get(0).fill_date();
        for (int i = 0; i < b.get(0).dates_2.size() - 1; i++) {
            a.get(i).fill_date();
            if (Integer.parseInt(b.get(0).expenses_and_incomes[i][0]) == a.get(i).difference) {
                System.out.println("RESPECT");
            } else {
                System.out.println("Найдено несоответствие в данном месяце: " + a.get(i).month);
                System.exit(0);
            }
        }
        System.out.println("Всё хорошо! Всего доброго!!!");


    }

    public static void information_about_months(ArrayList<MonthlyReport> a) {
        String b;
        for (MonthlyReport monthlyReport : a) {
            b = monthlyReport.profitable_and_not_profitable_products();
            System.out.println(b);
            System.out.println();
        }

    }

    public static void information_about_year(ArrayList<YearlyReport> a, ArrayList<MonthlyReport> m) {
        double b = 0;
        double c = 0;
        System.out.println("Рассматриваемый год: " + a.get(0).year);
        for (int i = 0; i < a.get(0).expenses_and_incomes.length; i++) {
            System.out.println("Прибыль месяца " + (i + 1) + " = " + m.get(i).difference);
            if(a.get(0).expenses_and_incomes[i][1].equals("+")){
                b += Integer.parseInt(a.get(0).expenses_and_incomes[i][0]);
            } else {
                c += Integer.parseInt(a.get(0).expenses_and_incomes[i][0]);
            }
        }
        System.out.println("-----------------------");
        System.out.println("Средний доход по месяцам = " + (b/m.size()));
        System.out.println("-----------------------");
        System.out.println("Средний расход по месяцам = " + (c/m.size()));
        System.out.println("-----------------------");




    }

    public static void main(String[] args) {
        ArrayList<MonthlyReport> months = new ArrayList<>();
        ArrayList<YearlyReport> years = new ArrayList<>();
        Scanner v = new Scanner(System.in);
        String connect;
        System.out.println("Do you want to continue?");
        System.out.println("y/n");
        int a;
        connect = v.nextLine();
        if (connect.equals("n")) {
            System.out.println("Goodbye!!!!");
            System.exit(0);
        } else {
            while (!(connect.equals("q"))) {
                System.out.println("Pick one of them");
                System.out.println("1. Считать все месячные отчёты");
                System.out.println("2. Считать годовой отчёт");
                System.out.println("3. Сверить отчёты");
                System.out.println("4. Вывести информацию о всех месячных отчётах");
                System.out.println("5. Вывести информацию о годовом отчёте");
                a = v.nextInt();
                connect = v.nextLine();
                if ((a < 1) || (a > 5)) {
                    System.out.println("Вы совершили ошибку");
                    continue;
                }
                switch (a) {
                    case 1 -> {
                        if (months.size() == 0) {
                            read_monthly_reports(months);
                        } else {
                            System.out.println("You have already gone this step!");
                            System.out.println("----------------------------------");
                        }


                    }
                    case 2 -> {
                        if (years.size() == 0) {
                            read_year_reports(years);
                        } else {
                            System.out.println("You have already gone this step!");
                            System.out.println("----------------------------------");
                        }


                    }
                    case 3 -> {
                        if (months.size() == 0) {
                            System.out.println("Вам следует считать месячные отчёты");
                            break;
                        } else if (years.size() == 0) {
                            System.out.println("Вам следует считать годовой отчёт");
                            break;
                        }
                        check_reports(months, years);

                    }
                    case 4 -> {
                        if (months.size() == 0) {
                            System.out.println("Вам следует считать месячные отчёты");
                            break;
                        }
                        if((months.get(0).incomes != 0) || (months.get(0).expenses != 0)){
                            information_about_months(months);
                        }else {
                            System.out.println("Пройдите предыдущие этапы");
                        }
                    }
                    case 5 -> {
                        if (months.size() == 0) {
                            System.out.println("Вам следует считать месячные отчёты");
                            break;
                        }
                        if((months.get(0).incomes != 0) || (months.get(0).expenses != 0)){
                            information_about_year(years, months);
                        } else {
                            System.out.println("Пройдите предыдущие этапы");
                        }

                    }

                }
                System.out.println("----------------------------------");
                System.out.println("Pick q if you want to quiet!");
                System.out.println("----------------------------------");
                System.out.println("If you want to continue just pick another letter or number");
                connect = v.nextLine();
            }
        }


    }
}
