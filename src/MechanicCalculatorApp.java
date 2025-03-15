import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MechanicCalculatorApp extends JFrame {
    private JComboBox<String> gamaValue;
    private JPanel mainPanel;
    private JButton calculateFields;
    private JRadioButton vehicleRadioBtn;
    private JRadioButton helicopterRadioBtn;
    private JLabel vehicleTypeLabel;
    private JCheckBox isFullTuningChkBx;
    private JSpinner cosmeticSpinnerValue;
    private JLabel cosmeticLabel;
    private JLabel carValueLabel;
    private JLabel blindajeLabel;
    private JComboBox<Integer> actualLevelBCbBx;
    private JLabel actualLevelLabel;
    private JLabel desiredLevelLabel;
    private JComboBox<Integer> desiredCbBx;
    private JCheckBox isPoliceChkbx;
    private JCheckBox washCarChk;
    private JCheckBox repairCarChk;
    private JButton clearButton;
    private JPanel finalPriceLbl;
    private JLabel totalPriceLabel;
    private JPanel secondPanel;
    private JPanel j2;
    private JSeparator field;
    private JSeparator f3;
    private JSeparator f4;
    private ButtonGroup vehicleTypeRadioGroup;

    private Map<Integer, Prices> vehiclePrices = new HashMap<>();


    public MechanicCalculatorApp() {
        setContentPane(mainPanel);
        setTitle("Mecánico");
        setSize(500, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setColors();

        isPoliceChkbx.setEnabled(false);

        vehicleTypeRadioGroup = new ButtonGroup();
        vehicleTypeRadioGroup.add(vehicleRadioBtn);
        vehicleTypeRadioGroup.add(helicopterRadioBtn);

        noChargeForCleaningAndRepairOnFullTuning();
        displayWarningForLevel5Blind();
        handleIfPolice();

        setToDefault();
        calculateTotalForJob();
    }

    private void noChargeForCleaningAndRepairOnFullTuning() {
        isFullTuningChkBx.addActionListener(e -> {
            boolean isFullTuning = isFullTuningChkBx.isSelected();

            if (isFullTuning) {
                washCarChk.setEnabled(false);
                repairCarChk.setEnabled(false);
                washCarChk.setSelected(false);
                repairCarChk.setSelected(false);
            } else {
                washCarChk.setEnabled(true);
                repairCarChk.setEnabled(true);
            }
        });
    }

    private void setColors() {
        j2.setBackground(Color.DARK_GRAY);
        mainPanel.setBackground(Color.DARK_GRAY);
        washCarChk.setBackground(Color.DARK_GRAY);
        secondPanel.setBackground(Color.DARK_GRAY);
        repairCarChk.setBackground(Color.DARK_GRAY);
        finalPriceLbl.setBackground(Color.DARK_GRAY);
        isPoliceChkbx.setBackground(Color.DARK_GRAY);
        vehicleRadioBtn.setBackground(Color.DARK_GRAY);
        totalPriceLabel.setBackground(Color.DARK_GRAY);
        isFullTuningChkBx.setBackground(Color.DARK_GRAY);
        helicopterRadioBtn.setBackground(Color.DARK_GRAY);

        gamaValue.setBackground(Color.GRAY);
        desiredCbBx.setBackground(Color.GRAY);
        actualLevelBCbBx.setBackground(Color.GRAY);
        cosmeticSpinnerValue.setBackground(Color.GRAY);

        gamaValue.setForeground(Color.WHITE);
        washCarChk.setForeground(Color.WHITE);
        desiredCbBx.setForeground(Color.WHITE);
        repairCarChk.setForeground(Color.WHITE);
        carValueLabel.setForeground(Color.WHITE);
        blindajeLabel.setForeground(Color.WHITE);
        cosmeticLabel.setForeground(Color.WHITE);
        vehicleRadioBtn.setForeground(Color.WHITE);
        totalPriceLabel.setForeground(Color.WHITE);
        actualLevelBCbBx.setForeground(Color.WHITE);
        actualLevelLabel.setForeground(Color.WHITE);
        vehicleTypeLabel.setForeground(Color.WHITE);
        desiredLevelLabel.setForeground(Color.WHITE);
        isFullTuningChkBx.setForeground(Color.WHITE);
        helicopterRadioBtn.setForeground(Color.WHITE);

        cosmeticSpinnerValue.getEditor().getComponent(0).setBackground(Color.GRAY);
        cosmeticSpinnerValue.getEditor().getComponent(0).setForeground(Color.WHITE);
    }

    private void calculateTotalForJob() {
        calculateFields.addActionListener(e -> {
            String vehicleType = determineVehicleTypeBySelection();
            int carValue = gamaValue.getSelectedIndex();
            Double total = 0.0;

            prepareCarPrices(vehicleType);

            if (washCarChk.isSelected())
                total += 1000.0;

            if (repairCarChk.isSelected())
                total += 1000.0;

            int actualBlindLevel = actualLevelBCbBx.getSelectedIndex();
            int desiredCheckLevel = desiredCbBx.getSelectedIndex();

            int blindLevelChange = Math.abs(actualBlindLevel - desiredCheckLevel);
            double blindLevelPrice = blindLevelChange * vehiclePrices.get(carValue).getBlindPrice();

            total += blindLevelPrice;

            total += (Integer) cosmeticSpinnerValue.getValue() * vehiclePrices.get(carValue).getCosmeticPrice();


            if (!isPoliceChkbx.isSelected() && isFullTuningChkBx.isSelected())
                total += vehiclePrices.get(carValue).getTuningPrice();
            else if (isPoliceChkbx.isSelected())
                total /= 2.0;

            totalPriceLabel.setText("Precio total: $" + String.format("%,.2f", total));
            vehiclePrices.clear();
        });
    }

    private void prepareCarPrices(String vehicleType) {
        if (vehicleType.equalsIgnoreCase("Vehículo")) {
            Prices vehiclePricesHigh = new Prices();
            Prices vehiclePricesMed = new Prices();
            Prices vehiclePricesLow = new Prices();

            vehiclePricesHigh.setTuningPrice(75000.00);
            vehiclePricesHigh.setBlindPrice(10000.00);
            vehiclePricesHigh.setCosmeticPrice(5000.00);

            vehiclePricesMed.setTuningPrice(28000.00);
            vehiclePricesMed.setBlindPrice(10000.00);
            vehiclePricesMed.setCosmeticPrice(2000.00);

            vehiclePricesLow.setTuningPrice(10000.00);
            vehiclePricesLow.setBlindPrice(10000.00);
            vehiclePricesLow.setCosmeticPrice(1000.00);

            vehiclePrices.put(0, vehiclePricesHigh);
            vehiclePrices.put(1, vehiclePricesMed);
            vehiclePrices.put(2, vehiclePricesLow);

        } else {
            Prices helicopterPricesHigh = new Prices();
            Prices helicopterPricesMed = new Prices();
            Prices helicopterPricesLow = new Prices();

            helicopterPricesHigh.setTuningPrice(300000.00);
            helicopterPricesHigh.setBlindPrice(20000.00);
            helicopterPricesHigh.setCosmeticPrice(30000.00);

            helicopterPricesMed.setTuningPrice(200000.00);
            helicopterPricesMed.setBlindPrice(20000.00);
            helicopterPricesMed.setCosmeticPrice(20000.00);

            helicopterPricesLow.setTuningPrice(100000.00);
            helicopterPricesLow.setBlindPrice(20000.00);
            helicopterPricesLow.setCosmeticPrice(10000.00);

            vehiclePrices.put(0, helicopterPricesHigh);
            vehiclePrices.put(1, helicopterPricesMed);
            vehiclePrices.put(2, helicopterPricesLow);
        }
    }

    private String determineVehicleTypeBySelection() {
        for (Enumeration<AbstractButton> buttons = vehicleTypeRadioGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected())
                return button.getText();
        }

        return "Vehículo";
    }

    public static void main(String[] args) {
        MechanicCalculatorApp app = new MechanicCalculatorApp();
    }

    private void createUIComponents() {
        String[] gamaValues = {"Alta", "Media", "Baja"};
        Integer[] blindValues = {0, 1, 2, 3, 4, 5};

        gamaValue = new JComboBox<>(gamaValues);
        actualLevelBCbBx = new JComboBox<>(blindValues);
        desiredCbBx = new JComboBox<>(blindValues);
    }

    private void handleIfPolice() {
        isPoliceChkbx.addActionListener(e -> {
            boolean isSelected = isPoliceChkbx.isSelected();

            isFullTuningChkBx.setEnabled(!isSelected);
            isFullTuningChkBx.setSelected(false);
        });
    }

    private void displayWarningForLevel5Blind() {
        String maxBlindMessage = "Advierta al cliente de que esto reducirá la velocidad máxima. El blindaje recomendado es de nivel 3";
        String title = "¡Importante! ¡Acción requerida!";
        desiredCbBx.addActionListener(e -> {
            int selectedItem = desiredCbBx.getSelectedIndex();

            if (selectedItem == 5)
                JOptionPane.showMessageDialog(mainPanel, maxBlindMessage, title, JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void setToDefault() {
        clearButton.addActionListener(e -> {
            vehicleRadioBtn.setSelected(true);
            helicopterRadioBtn.setSelected(false);

            isPoliceChkbx.setSelected(false);
            isFullTuningChkBx.setSelected(false);
            isFullTuningChkBx.setEnabled(true);
            washCarChk.setSelected(false);
            repairCarChk.setSelected(false);

            gamaValue.setSelectedIndex(0);

            actualLevelBCbBx.setSelectedIndex(0);
            desiredCbBx.setSelectedIndex(0);

            cosmeticSpinnerValue.setValue(0);

            totalPriceLabel.setText("Precio total: $0.00");

            washCarChk.setEnabled(true);
            repairCarChk.setEnabled(true);
        });
    }
}
