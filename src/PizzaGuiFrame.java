import javax.swing.*;
import java.awt.*;

public class PizzaGuiFrame extends JFrame {
    private JRadioButton thinCrustBtn;
    private JRadioButton regularCrustBtn;
    private JRadioButton deepDishCrustBtn;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppings;
    private JTextArea order;
    private JButton orderBtn;
    private JButton clearBtn;
    private JButton quitBtn;

    private static final double TAX_RATE = 0.07; // Tax rate set to 7%

    public PizzaGuiFrame() {
        setTitle("Pizza Order");
        setSize(700, 500); // Adjusted size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add padding between components
        getContentPane().setBackground(new Color(230, 230, 250)); // Light lavender background for frame

        // Layout and add all panels
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        // Crust panel
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Crust Type"));
        crustPanel.setBackground(new Color(255, 255, 204)); // Light yellow background
        thinCrustBtn = new JRadioButton("Thin");
        regularCrustBtn = new JRadioButton("Regular");
        deepDishCrustBtn = new JRadioButton("Deep-dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrustBtn);
        crustGroup.add(regularCrustBtn);
        crustGroup.add(deepDishCrustBtn);

        crustPanel.add(thinCrustBtn);
        crustPanel.add(regularCrustBtn);
        crustPanel.add(deepDishCrustBtn);

        // Size panel
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Pizza Size"));
        sizePanel.setBackground(new Color(255, 255, 204)); // Same light yellow background
        sizeComboBox = new JComboBox<>(new String[]{"Small", "Medium", "Large", "Super"});
        sizePanel.add(sizeComboBox);

        // Combine crust and size panels
        JPanel topSubPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topSubPanel.add(crustPanel);
        topSubPanel.add(sizePanel);
        topPanel.add(topSubPanel, BorderLayout.NORTH);

        return topPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0)); // Add space between panels
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        centerPanel.add(createToppingsPanel());
        centerPanel.add(createSummaryPanel());

        return centerPanel;
    }

    private JPanel createToppingsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10)); // Toppings in a 3x2 grid with padding
        panel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        panel.setBackground(new Color(204, 255, 204)); // Light green background

        String[] toppingNames = {"Pepperoni", "Mushrooms", "Onions", "Bacon", "Olives", "Pineapple", "Cheese", "Meatball", "Green Pepper", "Basil"};
        toppings = new JCheckBox[toppingNames.length];
        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            toppings[i].setBackground(new Color(204, 255, 204)); // Same light green background for checkboxes
            panel.add(toppings[i]);
        }

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        panel.setBackground(new Color(204, 229, 255)); // Light blue background

        order = new JTextArea(10, 30);
        order.setEditable(false);
        order.setBackground(new Color(230, 247, 255)); // Slightly lighter blue background for text area
        JScrollPane scrollPane = new JScrollPane(order);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center buttons with padding
        panel.setBackground(new Color(240, 240, 240)); // Light gray background

        orderBtn = createButton("Order", e -> displayOrder());
        clearBtn = createButton("Clear", e -> clearForm());
        quitBtn = createButton("Quit", e -> quitProgram());

        panel.add(orderBtn);
        panel.add(clearBtn);
        panel.add(quitBtn);
        return panel;
    }

    private void displayOrder() {
        String crust = thinCrustBtn.isSelected() ? "Thin" :
                regularCrustBtn.isSelected() ? "Regular" : "Deep-dish";
        String size = (String) sizeComboBox.getSelectedItem();
        double basePrice = getPrice(size);
        StringBuilder orderText = new StringBuilder("=========================================\n")
                .append("Crust: ").append(crust).append(", Size: ").append(size)
                .append("\n-----------------------------------------\nToppings:\n");

        double toppingPrice = 0;
        for (JCheckBox topping : toppings) {
            if (topping.isSelected()) {
                orderText.append(topping.getText()).append("\t\t$1.00\n");
                toppingPrice += 1.0;
            }
        }

        double subtotal = basePrice + toppingPrice;
        double tax = subtotal * TAX_RATE;
        double total = subtotal + tax;

        order.setText(orderText
                .append("-----------------------------------------\n")
                .append("Subtotal:\t\t$").append(String.format("%.2f", subtotal)).append("\n")
                .append("Tax (7%):\t\t$").append(String.format("%.2f", tax)).append("\n")
                .append("-----------------------------------------\n")
                .append("Total:\t\t$").append(String.format("%.2f", total))
                .append("\n=========================================\n").toString());
    }

    private void clearForm() {
        thinCrustBtn.setSelected(false);
        regularCrustBtn.setSelected(false);
        deepDishCrustBtn.setSelected(false);
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) topping.setSelected(false);
        order.setText("");
    }

    private void quitProgram() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) System.exit(0);
    }

    private double getPrice(String size) {
        return switch (size) {
            case "Small" -> 8.00;
            case "Medium" -> 12.00;
            case "Large" -> 16.00;
            case "Super" -> 20.00;
            default -> 0.00;
        };
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(30, 144, 255)); // Dodger blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }
}
