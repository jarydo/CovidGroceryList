import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainMenu {
    private static boolean passwordCorrect = false;
    private static String password;
    private static String filename;
    private static String name;
    private static int amount;
    private static double cost;
    private static String location;
    private static boolean isLoggedIn;
    private static ArrayList<Item> userList = new ArrayList<Item>();
    private static ArrayList<Item> mainList = new ArrayList<Item>();
    private static boolean DS;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) throws Exception{

        //Determines whether user is the designated shopper or not
        System.out.println("Are you the designated shopper?");
        Scanner sc = new Scanner(System.in);
        if (sc.nextLine().equalsIgnoreCase("no")) {
            DS = false;
            //Tries to sign in, if it fails, runs register
            boolean hasLoggedInBefore;
            try{
                SignIn();
                hasLoggedInBefore = true;
            } catch(Exception e){
                Register();
                hasLoggedInBefore = false;
            }

            do {
                Item i = new Item("name", "location", 0, 0, 0);
                //if they have logged in before, read through the file to add to userList
                if(hasLoggedInBefore == true){
                    FileReader fr = new FileReader(filename);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    br.readLine();
                    do {
                        line = br.readLine();
                        if(line!= null){
                            i = new Item("name", "location", 0, 0, 0);
                            i.setName(line);
                            line=br.readLine();
                            i.setLocation(line);
                            line=br.readLine();
                            i.setCost(Double.parseDouble(line));
                            line = br.readLine();
                            i.setAmount(Integer.parseInt(line));
                            line = br.readLine();
                            i.setTotalCost(Double.parseDouble(line));
                            userList.add(i);
                        }
                    } while (line != null);
                    br.close();
                    hasLoggedInBefore = false;
                } else{
                    System.out.println("What item would you like to add? (Type done when you are finished, remove if you want to remove an item)");
                    sc = new Scanner(System.in);
                    name = sc.nextLine().toLowerCase();
                    if (name.equals("done")) {
                        isLoggedIn = false;
                    } else if(name.equals("remove")){
                        removeItem();
                    } else {
                        System.out.println("How many?");
                        sc = new Scanner(System.in);
                        amount = sc.nextInt();

                        try {
                            //read through database until item is found. If not found, triggers instance of addNewItem
                            FileReader fr = new FileReader("database.txt");
                            BufferedReader br = new BufferedReader(fr);
                            String line;
                            do {
                                line = br.readLine();
                            } while (!line.equals(name));
                            cost = Double.parseDouble(br.readLine());
                            location = br.readLine();
                        } catch (Exception e) {
                            addNewItem();
                        }

                        //sets everything
                        i.setName(name);
                        i.setLocation(location);
                        i.setCost(cost);
                        i.setAmount(amount);

                        //reads through Items to check if item already exists. If so, adds the amounts together
                        for(int x=0; x<userList.size(); x++){
                            if(userList.get(x).getName().equals(name)){
                                i.setAmount(userList.get(x).getAmount()+amount);
                                userList.remove(x);
                            }
                        }

                        i.setTotalCost(i.getAmount()*i.getCost());
                        userList.add(i);
                    }
                }
            } while (isLoggedIn == true);

            boolean viewFinishedList = false;
            double finalCost = 0;

            sc = new Scanner(System.in);
            System.out.println("Would you like to see the finished list?");
            if(sc.nextLine().equalsIgnoreCase("yes")){
                viewFinishedList = true;
                System.out.println("Item: \t\t\t Amount: \t\t\t Location: \t\t\t Cost: \t\t\t Total Cost:");
            }

            Collections.sort(userList);
            FileWriter fw = new FileWriter(filename);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(password);
            for (Item i : userList) {
                pw.println(i.getName());
                pw.println(i.getLocation());
                pw.println(df.format(i.getCost()));
                pw.println(i.getAmount());
                pw.println(df.format(i.getTotalCost()));
                finalCost = finalCost + i.getTotalCost();
                if(viewFinishedList){
                    System.out.println(i.toString());
                }
            }
            System.out.println();
            System.out.println("Final Cost: \t\t\t\t\t$" + df.format(finalCost));

            pw.close();
            mainList();
        }
        else { //if the user is the designated shopper
            DS = true;
            mainList();
        }
    }

    public static void SignIn() throws Exception{
        Scanner sc = new Scanner (System.in);
        System.out.println("Please input your username");
        User us1 = new User(sc.nextLine());
        filename = us1.getUsername() + ".txt";

        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        password = br.readLine();

        String uPassword;

        Scanner sc1 = new Scanner(System.in);
        System.out.println("Please enter password:");
        for(int i=1; i<=3; i++){
            uPassword = sc1.nextLine();
            if(password.equals(uPassword)){
                System.out.println("Welcome back!");
                i = 4;
                isLoggedIn = true;
            } else{
                System.out.println("Sorry, that is not your password. You have " + (3-i) + " tries left");
            }
        }
    }

    public static void Register() throws Exception{
        boolean usernameExists = true;
        Scanner sc = new Scanner (System.in);
        //change this output based on the main method
        System.out.println("It appears as if you have never logged in before. What would you like your username to be?");
        User us1 = new User(sc.nextLine());
        String filename = us1.getUsername() + ".txt";
        FileWriter fw = new FileWriter("nameList.txt",true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(filename);
        pw.close();

        while(usernameExists == true){
            try{
                FileReader fr = new FileReader(filename);
                System.out.println("Sorry, this username exists. Please pick another!");
                filename = sc.nextLine() + ".txt";
            }catch(Exception e){
                usernameExists = false;
            }
        }

        Scanner sc1 = new Scanner(System.in);
        System.out.println("What would you like your password to be?");
        password = sc1.nextLine();
        System.out.println("Please confirm password:");
        while(passwordCorrect == false){
            if(password.equals(sc1.nextLine())){
                passwordCorrect = true;
            }else{
                System.out.println("Sorry, that is not the same password. Try again");
            }
        }
        System.out.println("Thanks for registering!");
        isLoggedIn = true;
    }

    public static void addNewItem() throws Exception {
        System.out.println("It appears that "+ name +" is not currently in our database. Please enter the following information to add it.");
        Scanner sc = new Scanner(System.in);
        System.out.println("What is the cost of your item (no $ sign)?");
        cost = sc.nextDouble();
        sc = new Scanner(System.in);
        System.out.println("Where is your item located? Please choose one of: Produce, Frozen, Meats, Dairy, Bakery, Snacks, Essential Supplies");//specify specific parts of store
        location = sc.nextLine();

        FileWriter fw = new FileWriter("database.txt",true);
        PrintWriter pw = new PrintWriter(fw);

        pw.println();
        pw.println(name);
        pw.println(cost);
        pw.println(location);

        pw.close();
    }

    public static void removeItem() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("What item would you like to remove?");
        String toRemove = sc.nextLine().toLowerCase();
        System.out.println("How many would you like to remove?");
        int numRemoved = sc.nextInt();
        for(int i=0; i<userList.size(); i++){
            if(userList.get(i).getName().equals(toRemove)){
                if(userList.get(i).getAmount() <= numRemoved){
                    userList.remove(i);
                }else{
                    userList.get(i).setAmount(userList.get(i).getAmount() - numRemoved);
                    userList.get(i).setTotalCost(userList.get(i).getAmount()*userList.get(i).getCost());
                }
            }
        }
    }

    public static void mainList()throws Exception {
        Item i;
        double finalCost = 0;

        FileReader fr = new FileReader("nameList.txt");
        BufferedReader br = new BufferedReader(fr);
        String UN;//will make a string of the username

        do {
            UN = br.readLine();
            if(UN != null) {
                FileReader fr2 = new FileReader(UN);
                BufferedReader br2 = new BufferedReader(fr2);
                String line;
                br2.readLine();
                do {
                    line = br2.readLine();
                    if (line != null) {
                        i = new Item("name", "location", 0, 0, 0);
                        i.setName(line);
                        line = br2.readLine();
                        i.setLocation(line);
                        line = br2.readLine();
                        i.setCost(Double.parseDouble(line));
                        line = br2.readLine();
                        i.setAmount(Integer.parseInt(line));
                        line = br2.readLine();
                        i.setTotalCost(Double.parseDouble(line));
                        for(int x=0; x<mainList.size(); x++){
                            if(mainList.get(x).getName().equals(i.getName())){
                                i.setAmount(mainList.get(x).getAmount()+i.getAmount());
                                i.setTotalCost(mainList.get(x).getTotalCost()+i.getTotalCost());
                                mainList.remove(x);
                            }
                        }
                        mainList.add(i);
                    }
                } while (line != null);
                br2.close();
            }
        } while (UN != null);
        br.close();
        Collections.sort(mainList);


        if (DS == false) {
            FileWriter fw2 = new FileWriter("mainList.txt");
            PrintWriter pw2 = new PrintWriter(fw2);
            for (Item i2 : mainList) {
                pw2.println(i2.getName());
                pw2.println(i2.getLocation());
                pw2.println(df.format(i2.getCost()));
                pw2.println(i2.getAmount());
                pw2.println(df.format(i2.getTotalCost()));
            }
            pw2.close();
        } else {
            FileWriter fw2 = new FileWriter("FinalMainList.txt");
            PrintWriter pw2 = new PrintWriter(fw2);
            pw2.println("Item: \t\t\t Amount: \t\t\t Location: \t\t\t Cost: \t\t\t Total Cost:");
            System.out.println("Item: \t\t\t Amount: \t\t\t Location: \t\t\t Cost: \t\t\t Total Cost:");
            for (Item i2 : mainList) {
                pw2.println(i2.toString());
                System.out.println(i2.toString());
                finalCost = finalCost + i2.getTotalCost();
            }
            pw2.println();
            pw2.println("Final Cost: \t\t\t\t\t$" + df.format(finalCost));
            pw2.close();

            System.out.println();
            System.out.println("Final Cost: \t\t\t\t\t$" + df.format(finalCost));
        }
    }
}
