/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author doria
 */
public class ConditionalCheckBoxRenderer extends JPanel implements TableCellRenderer {

        private static final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
        private JCheckBox cb;

        public ConditionalCheckBoxRenderer() {
            setLayout(new GridBagLayout());
            setOpaque(false);
            cb = new JCheckBox();
            cb.setOpaque(false);
            cb.setContentAreaFilled(false);
            cb.setMargin(new Insets(0, 0, 0, 0));
            add(cb);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setOpaque(isSelected);
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
            }
            if (value instanceof Integer) {
                int state = (int) value;
                cb.setVisible(state != 0);
                cb.setSelected(state == 2);
            }
            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(NO_FOCUS_BORDER);
            }
            return this;
        }
    }