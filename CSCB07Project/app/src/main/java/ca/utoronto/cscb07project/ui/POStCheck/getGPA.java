package ca.utoronto.cscb07project.ui.POStCheck;

import java.text.DecimalFormat;
import java.util.HashMap;

public class getGPA {

    // is making these methods static going to cause issues?
    public static boolean passedReq1(double a67, double a48, double a22, double a31, double a37){
        double cgpa = (findGPA(a67) + findGPA(a48) + findGPA(a22) + findGPA(a31) + findGPA(a37))/5;
        return cgpa >= 2.5;
    }

    public static double gpaCalc(double a67, double a48, double a22, double a31, double a37){
        double GPA = (findGPA(a67) + findGPA(a48) + findGPA(a22) + findGPA(a31) + findGPA(a37))/5;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String roundGPA = decimalFormat.format(GPA);
        return Double.parseDouble(roundGPA);
    }

    public static boolean passedReq2(double a48){
        return findGPA(a48) >= 3.0;
    }

    public static boolean passedReq3(double a67, double a22, double a37){
        if(findGPA(a67) >= 1.7 && findGPA(a22) >= 1.7){
            return true;
        }
        else if(findGPA(a67) >= 1.7 && findGPA(a37) >= 1.7){
            return true;
        }
        else if(findGPA(a22) >= 1.7 && findGPA(a37) >= 1.7){
            return true;
        }
        else if(findGPA(a37) >= 1.7 && findGPA(a67) >= 1.7){
            return true;
        }
        else{
            return false;
        }
    }


    public static double findGPA(double grade){
        //return 4.0;

        if(grade >= 85 && grade <= 100){
            return 4.0;
        }
        else if(grade >= 80 && grade <= 84){
            return 3.7;
        }
        else if(grade >= 77 && grade <= 79){
            return 3.3;
        }
        else if(grade >= 73 && grade <= 76){
            return 3.0;
        }
        else if(grade >= 70 && grade <= 72){
            return 2.7;
        }
        else if(grade >= 67 && grade <= 69){
            return 2.3;
        }
        else if(grade >= 63 && grade <= 66){
            return 2.0;
        }
        else if(grade >= 60 && grade <= 62){
            return 1.7;
        }
        else if(grade >= 57 && grade <= 59){
            return 1.3;
        }
        else if(grade >= 53 && grade <= 56){
            return 1.0;
        }
        else if(grade >= 50 && grade <= 52){
            return 0.7;
        }
        else{
            return 0.0;
        }


    }
}
