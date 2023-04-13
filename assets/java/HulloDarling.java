import java.io.Serializable;

public class HulloDarling implements Serializable {

    private static final long serialVersionUID = -6991196364681901587L;

    private static final String GREETING = "Hullo, my dear darling!";

    public static void main(String[] args) {
        System.out.println(GREETING);
    }

    static String getGreeting() {
        return GREETING;
    }

}