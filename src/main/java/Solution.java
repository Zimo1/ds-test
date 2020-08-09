/*  Тестовое задание Java. Суслопаров А.В., август 2020 г.
1)  Написать приложение на вход которого приходит массив чисел необходимо вывести на консоль три списка
        числа которые можно поделить без остатка на 3
        числа которые можно поделить без остатка на 7
        числа которые можно поделить без остатка на 21
2)  необходимо реализовать команды
        init array	- инициализация списков набором значений array
        print 		- печать всех списков
        print type 	- печать конкретного списка, где type принимает значения X,S,M
        anyMore		- выводит на экран были ли значения не вошедшие ни в один список, возможные значения true, false
        clear type	- чистка списка , где type принимает значения X,S,M
        merge		- слить все списки в один вывести на экран и очистить все списки
        help		- вывод справки по командам

        для каждого набора значения должы быть отсортированы в порядке возрастания
        если какой то из списков пустой необходимо напечатать сообщение "Список type пуст" при выводе списка с помощью команд Print
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    // Константы типов списков (значения из условий задачи)
    private static final String LIST3_TYPE_CHAR = "X";
    private static final String LIST7_TYPE_CHAR = "S";
    private static final String LIST21_TYPE_CHAR = "M";
    // Три списка, на которые делится входящий список
    private static ArrayList<Integer> list3 = new ArrayList<Integer>();
    private static ArrayList<Integer> list7 = new ArrayList<Integer>();
    private static ArrayList<Integer> list21 = new ArrayList<Integer>();
    // Переменная anyMore показывает, были ли во входном списке числа, не делящиеся на 3, 7, 21
    private static Boolean anyMore = false;

    public static void main(String[] args) throws IOException {
        // Инициализация значениями по умолчанию, для демонстрации работы программы.
        // В дальнейшем при помощи команды INIT можно ввести с клавиатуры любые другие значения
        ArrayList<Integer> startlist = new ArrayList(Arrays.asList(1, 3, 6, 7, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36,
                                                                    14, 28, 35, 42, 63, 56, 70, -21, -42, -6, -14));
        System.out.println("Разделение списка, введеного по умолчанию, на 3 списка:");
        divideList(startlist); // Разделение значений на 3 списка
        printAllLists(); // Вывод списков на экран
        // Подготовка к приему ввода команд с клавиатуры
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Цикл обработки команд с клавиатуры
        while (true)
        {
            System.out.println("Введите команду (help - выводит список команд). Для выхода нажмите Enter:");
            String s = reader.readLine(); //Получить строку с клавиатуры
            s = s.trim(); //Очистить строку от пробелов спереди и сзади
            if (s.isEmpty()) break; //Если ничего не введено (нажат Enter) - прекратить цикл и закончить программу
            // Иначе разобрать введенную строку.
            // Первое слово должно содержать определенную команду. Она отделяется от своих параметров (если они есть) пробелом.
            // Чтобы выделить из строки команду, нужно найти позицию первого слева пробела
            int spacePos = s.indexOf(" ");
            String command = "";
            String parameters = "";
            // Если пробел в строке отсутствует, это значит, что у команды нет параметров.
            // Все команды и параметры приводятся к верхнему регистру для единообразия и избежания ошибок при обработке
            if (spacePos == -1) command = s.toUpperCase();
            else {
                command = s.substring(0, spacePos).toUpperCase();
                parameters = s.substring(spacePos + 1).trim().toUpperCase();
            }
            //Обработка полученных команд
            switch (command) {
                case "INIT": {
                    // Инициализация - ввод новых значений с клавиатуры. После команды INIT должны идти числа,
                    // которые нужно распознать и занести в список
                    if (parameters.isEmpty()) System.out.println("Ошибка. У команды INIT должны быть параметры. Введите числа");
                    else { //Обработка строки параметров при помощи регулярного выражения. Запись полученных чисел в список
                        Matcher m = Pattern.compile("-?\\d+").matcher(parameters);
                        startlist.clear();
                        while (m.find()) { //Разбор строки параметров (чисел)
                            Integer element = Integer.parseInt(m.group());
                            // Повторяющиеся значения приняты не будут, для избежания дублирования
                            if (!startlist.contains(element)) startlist.add(element);
                        }
                        divideList(startlist); // Разделение полученных значений на 3 списка
                        System.out.println("Разделение введенного списка чисел на 3 списка:");
                        printAllLists(); //Вывод списков на экран
                    }
                    break;
                }
                case "PRINT": { // Вывод списков на экран
                    if (parameters.isEmpty()) printAllLists();
                    else
                        switch (parameters) {
                            case LIST3_TYPE_CHAR: {
                                printList(list3, "3");
                                break;
                            }
                            case LIST7_TYPE_CHAR: {
                                printList(list7, "7");
                                break;
                            }
                            case LIST21_TYPE_CHAR: {
                                printList(list21, "21");
                                break;
                            }
                            default: {
                                System.out.println("Ошибка. Введен неверный параметр команды PRINT: " + parameters);
                                System.out.println("Допустимые значения: " + LIST3_TYPE_CHAR + " " +
                                                     LIST7_TYPE_CHAR + " " + LIST21_TYPE_CHAR);
                            }
                        }
                    break;
                }
                case "ANYMORE": { // При разделении на 3 списка в переменной anyMore отмечается наличие чисел,
                    // не делящихся на 3, 7, 21
                    System.out.println("anyMore = " + anyMore);
                    break;
                }
                case "CLEAR": { // Очистка заданного параметром списка
                    switch (parameters) {
                        case LIST3_TYPE_CHAR: {
                            list3.clear();
                            System.out.println("Список чисел, делящихся без остатка на 3, очищен");
                            break;
                        }
                        case LIST7_TYPE_CHAR: {
                            list7.clear();
                            System.out.println("Список чисел, делящихся без остатка на 7, очищен");
                            break;
                        }
                        case LIST21_TYPE_CHAR: {
                            list21.clear();
                            System.out.println("Список чисел, делящихся без остатка на 21, очищен");
                            break;
                        }
                        default: {
                            System.out.println("Ошибка. Введен неверный параметр команды CLEAR: " + parameters);
                            System.out.println("Допустимые значения: " + LIST3_TYPE_CHAR + " " +
                                    LIST7_TYPE_CHAR + " " + LIST21_TYPE_CHAR);
                            System.out.println("Команда CLEAR без параметра недопустима");
                            break;
                        }
                    }
                    break;
                }
                case "MERGE": { // Слияние всех списков в один, вывод его на экран, очистка всех списков
                    // Для объединения списков применяется временное множество, т.к. оно позволяет автоматически
                    // избежать дублирования повторяющихся значений
                    SortedSet<Integer> tmpSet = new TreeSet<>();
                    tmpSet.addAll(list3);
                    tmpSet.addAll(list7);
                    tmpSet.addAll(list21);
                    startlist.clear();
                    startlist = new ArrayList<>(tmpSet);
                    System.out.println("Объединенный отсортированный список без повторов:");
                    printList(startlist, "");
                    list3.clear();
                    list7.clear();
                    list21.clear();
                    startlist.clear();
                    System.out.println("Все списки очищены");
                    break;
                }
                case "HELP": {
                    System.out.println("СПРАВКА ПО КОМАНДАМ (допустим ввод в любом регистре):");
                    System.out.println("init array	- инициализация списков набором целых значений array, значения вводятся через пробел, запятую и т.п.");
                    System.out.println("print 		- печать всех списков");
                    System.out.println("print type 	- печать конкретного списка, где type принимает значения " + LIST3_TYPE_CHAR + ", " +
                                        LIST7_TYPE_CHAR + ", " + LIST21_TYPE_CHAR);
                    System.out.println("anyMore		- выводит на экран были ли значения не вошедшие ни в один список, возможные значения true, false");
                    System.out.println("clear type	- чистка списка, где type принимает значения " + LIST3_TYPE_CHAR + ", " +
                                        LIST7_TYPE_CHAR + ", " + LIST21_TYPE_CHAR);
                    System.out.println("merge		- слить все списки в один, вывести на экран и очистить все списки");
                    System.out.println("help		- вывод справки по командам");
                    break;
                }
                default:
                    System.out.println("Ошибка. Введена неверная команда: " + command);
            }
        }
    }

    //Метод предназанчен для разделения входящего списка list на 3 списка по условиям задачи
    static void divideList(ArrayList<Integer> list) {
        anyMore = false;
        list3.clear();
        list7.clear();
        list21.clear();
        for (Integer element : list) { // Цикл по всем элементам входящего списка
            if (element % 21 == 0) { // Если число делится нацело на 21, то значит оно делится и на 3 и на 7.
                // Поэтому такое число заносится сразу во все 3 списка
                list21.add(element);
                list7.add(element);
                list3.add(element);
            }
            else
                if (element % 7 == 0) list7.add(element); // Число делится нацело на 7
                else
                    if (element % 3 == 0) list3.add(element); // Число делится нацело на 3
                    else anyMore = true; //Если число не делится на 3, 7, 21, значит нужно установить anyMore в true

        }
        // Отсортировать полученные 3 списка по возрастанию (по условию задачи)
        Collections.sort(list3);
        Collections.sort(list7);
        Collections.sort(list21);
    }

    // Метод предназначен для вывода на экран содержимого 1 входящего списка
    static void printList(ArrayList<Integer> list, String divider) {
        if (!divider.isEmpty()) System.out.println("Список чисел, делящихся без остатка на " + divider + ":");
        if (list.isEmpty()) System.out.print("Список пуст");
        else
            list.forEach(s -> System.out.print(s + " "));
        System.out.println();
    }

    // Метод предназначен для вывода на экран всех трех списков сразу
    static void printAllLists() {
        System.out.print("1. ");
        printList(list3, "3");
        System.out.print("2. ");
        printList(list7, "7");
        System.out.print("3. ");
        printList(list21, "21");
    }
}
