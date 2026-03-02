import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PizzaGUIFrame extends JFrame {

    JPanel mainPnl, topPnl, crustPnl, toppingPnl, displayPnl, cmdPnl;
    JLabel titleLbl;
    JTextArea displayTA;
    JScrollPane scroller;
    JButton quitBtn, fortuneBtn, clearBtn;
    JRadioButton thin, regular, deepDish;
    JComboBox<String> sizeCombo;
    JCheckBox sausage, pepperoni, pepper, olive, anchovies, pineapple;
    double pizzaPrice, pizzaTax, pizzaTotal;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzaGUIFrame());
    }

    public PizzaGUIFrame() {
        setTitle("Make Your Own Pizza");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPnl = new JPanel(new BorderLayout(10, 10));
        add(mainPnl);

        createTopPanel();
        createCrustAndToppingPanel();
        createDisplayPanel();
        createControlPanel();

        setVisible(true);
    }

    public void createTopPanel() {
        topPnl = new JPanel(new BorderLayout());

        titleLbl = new JLabel("Make Your Own Pizza", JLabel.CENTER);
        titleLbl.setFont(new Font("Serif", Font.BOLD, 28));
        topPnl.add(titleLbl, BorderLayout.NORTH);

        JPanel sizePnl = new JPanel();
        sizeCombo = new JComboBox<>();
        sizeCombo.addItem("Small");
        sizeCombo.addItem("Medium");
        sizeCombo.addItem("Large");
        sizeCombo.addItem("Extra Large");
        sizePnl.add(new JLabel("Select Size: "));
        sizePnl.add(sizeCombo);

        topPnl.add(sizePnl, BorderLayout.SOUTH);

        mainPnl.add(topPnl, BorderLayout.NORTH);
    }

    public void createCrustAndToppingPanel() {
        JPanel leftPnl = new JPanel();
        leftPnl.setLayout(new GridLayout(2, 1, 5, 5)); // Crust on top, Toppings below

        crustPnl = new JPanel(new GridLayout(1, 3));
        crustPnl.setBorder(BorderFactory.createTitledBorder("Select Crust"));

        thin = new JRadioButton("Thin", true);
        regular = new JRadioButton("Regular");
        deepDish = new JRadioButton("Deep Dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thin);
        crustGroup.add(regular);
        crustGroup.add(deepDish);

        crustPnl.add(thin);
        crustPnl.add(regular);
        crustPnl.add(deepDish);

        toppingPnl = new JPanel(new GridLayout(3, 2));
        toppingPnl.setBorder(BorderFactory.createTitledBorder("Select Toppings"));

        sausage = new JCheckBox("Sausages");
        pepperoni = new JCheckBox("Pepperoni");
        pepper = new JCheckBox("Peppers");
        olive = new JCheckBox("Olives");
        anchovies = new JCheckBox("Anchovies");
        pineapple = new JCheckBox("Pineapples");

        toppingPnl.add(sausage);
        toppingPnl.add(pepperoni);
        toppingPnl.add(pepper);
        toppingPnl.add(olive);
        toppingPnl.add(anchovies);
        toppingPnl.add(pineapple);

        leftPnl.add(crustPnl);
        leftPnl.add(toppingPnl);

        mainPnl.add(leftPnl, BorderLayout.WEST);
    }

    public void createDisplayPanel() {
        displayPnl = new JPanel();
        displayTA = new JTextArea(20, 30);
        displayTA.setEditable(false);
        scroller = new JScrollPane(displayTA);
        displayPnl.add(scroller);

        mainPnl.add(displayPnl, BorderLayout.CENTER);
    }

    public void createControlPanel() {
        cmdPnl = new JPanel();
        fortuneBtn = new JButton("Order Review");
        clearBtn = new JButton("Clear");
        quitBtn = new JButton("Quit");

        clearBtn.addActionListener((ActionEvent ae) -> displayTA.setText(""));
        quitBtn.addActionListener((ActionEvent ae) -> {
            int response = JOptionPane.showConfirmDialog(null, "Quit and discard Pizza?");
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        fortuneBtn.addActionListener((ActionEvent ae) -> displayTA.setText(getPizzaSummary()));

        cmdPnl.add(fortuneBtn);
        cmdPnl.add(clearBtn);
        cmdPnl.add(quitBtn);

        mainPnl.add(cmdPnl, BorderLayout.SOUTH);
    }

    private void pizzaPrice() {
        pizzaPrice = 0;
        String selectedSize = (String) sizeCombo.getSelectedItem();
        if (selectedSize.equals("Small")) {
            pizzaPrice = 8;
        } else if (selectedSize.equals("Medium")) {
            pizzaPrice = 12;
        } else if (selectedSize.equals("Large")) {
            pizzaPrice = 16;
        } else if (selectedSize.equals("Extra Large")) {
            pizzaPrice = 20;
        }

        if (sausage.isSelected()) {
            pizzaPrice++;
        }
        if (pepperoni.isSelected()) {
            pizzaPrice++;
        }
        if (pepper.isSelected()) {
            pizzaPrice++;
        }
        if (olive.isSelected()) {
            pizzaPrice++;
        }
        if (anchovies.isSelected()) {
            pizzaPrice++;
        }
        if (pineapple.isSelected()) {
            pizzaPrice++;
        }
    }

    private int countToppings() {
        int count = 0;
        for (JCheckBox cb : new JCheckBox[]{sausage, pepperoni, pepper, olive, anchovies, pineapple}) {
            if (cb.isSelected()) count++;
        }
        return count;
    }

    private String getPizzaSummary() {

        pizzaPrice();
        double subTotal = pizzaPrice;
        double tax = subTotal * 0.07;
        double total = subTotal + tax;

        StringBuilder sb = new StringBuilder();

        sb.append("=========================================\n");
        sb.append(String.format("%-30s %8s\n", "Item", "Price"));
        sb.append("=========================================\n");

        // Crust + Size line
        String crustType = thin.isSelected() ? "Thin" :
                regular.isSelected() ? "Regular" : "Deep Dish";

        String size = (String) sizeCombo.getSelectedItem();

        sb.append(String.format("%-30s $%7.2f\n",
                crustType + " - " + size,
                subTotal - countToppings()));  // base price only

        // Toppings
        for (JCheckBox cb : new JCheckBox[]{sausage, pepperoni, pepper, olive, anchovies, pineapple}) {
            if (cb.isSelected()) {
                sb.append(String.format("%-30s $%7.2f\n", cb.getText(), 1.00));
            }
        }

        sb.append("-----------------------------------------\n");
        sb.append(String.format("%-30s $%7.2f\n", "Sub-total:", subTotal));
        sb.append(String.format("%-30s $%7.2f\n", "Tax (7%):", tax));
        sb.append(String.format("%-30s $%7.2f\n", "Total:", total));
        sb.append("=========================================\n");

        return sb.toString();
    }
}

