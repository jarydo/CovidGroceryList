import java.text.DecimalFormat;

public class Item  implements Comparable <Item>{
    private String name;
    private String location;
    private double cost;
    private int amount;
    private int locationValue;
    private int letterValue;
    private double totalCost;
    private int i;

    public int compareTo (Item x) {
        this.i = 0;
        int diff = 0;//diff can be -1, 0 or 1.
        if (this.getLocationValue()< x.getLocationValue()){
            diff = -1;
        } else if (this.getLocationValue()> x.getLocationValue()){
            diff = 1;
        } else {//If we reach here, the values are the same (it's a tie)
            //these if statements will develop the tie breaker based on the name alphabetically.
            do {
                if (this.getLetterValue() < x.getLetterValue()) {
                    diff = -1;
                } else if (this.getLetterValue() > x.getLetterValue()) {
                    diff = 1;
                } else {
                    this.i++;
                }
            }while (diff == 0);
        }
        return diff;
    }

    public Item (String n, String l, double c, int a, double tc)throws Exception{
        setName(n);
        setLocation(l);
        setCost(c);
        setAmount(a);
        setTotalCost(tc);
    }
    //for loop with n.length
    public void setName (String n){
        this.name = n.toLowerCase();
    }

    public String getName(){
        return this.name;
    }

    public int getLetterValue(){
        char letter = this.getName().charAt(this.i);
        this.letterValue = ((int)letter );
        return this.letterValue;
    }

    public void setLocation (String l) {
        this.location = l.toLowerCase();

        if (this.location.equals("produce")){
            this.locationValue = 0;
        }
        else if (this.location.equals("frozen")){
            this.locationValue = 1;
        }
        else if (this.location.equals("meats")){
            this.locationValue = 2;
        }
        else if (this.location.equals("dairy")){
            this.locationValue = 3;
        }
        else if (this.location.equals("bakery")){
            this.locationValue = 4;
        }
        else if (this.location.equals("snacks")){
            this.locationValue = 5;
        }
        else if (this.location.equals("essential supplies")){
            this.locationValue = 6;
        }
    }

    public int getLocationValue(){
        return this.locationValue;
    }

    public String getLocation(){
        return this.location;
    }

    public void setCost(double c) throws Exception{
        if (c >= 0){
            this.cost = c;
        }
        else{
            throw new Exception();
        }
    }

    public double getCost(){
        return this.cost;
    }

    public void setTotalCost(double tc)throws Exception{//tc is the total cost
        if (tc >= 0){
            this.totalCost = tc;
        }
        else{
            throw new Exception();
        }
    }

    public double getTotalCost(){
        return this.totalCost;
    }

    public void setAmount(int a)throws Exception{
        if (a >= 0){
            this.amount = a;
        }
        else{
            throw new Exception();
        }
    }

    public int getAmount(){
        return this.amount;
    }

    public String toString(){
        DecimalFormat df = new DecimalFormat("0.00");
        return this.getName() + "\t\t\t\t" + this.getAmount() + "\t\t\t\t\t" + this.getLocation() + "\t\t\t\t" + df.format(this.getCost())+ "\t\t\t" + df.format(this.getTotalCost());
    }
}
