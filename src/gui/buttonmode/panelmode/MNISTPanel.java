package gui.buttonmode.panelmode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import gui.buttonmode.managermode.MNISTManager;
import gui.mode.ModeGUI;

/**
 * This class expands the ButtonPanel to the specific case of training the MNIST
 * handwritten digits dataset.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class MNISTPanel extends ButtonPanel {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This button, when clicked, will open a visualization of all the correctly
	 * classified digits of the neural net.
	 */
	public JButton visualizeCorrects;

	/**
	 * This button, when clicked, will open a visualization of all the falsely
	 * classified digits of the neural net.
	 */
	public JButton visualizeFalse;

	/**
	 * Setting up panel
	 * 
	 * @param width
	 *            width of panel
	 * @param height
	 *            height of panel
	 * @param mg
	 *            underlying modegui
	 */
	public MNISTPanel(int width, int height, ModeGUI mg) {
		super(width, height, mg);
		this.buttonManager = new MNISTManager(this);
		this.addActionListeners();
		this.visualizeCorrects = new JButton("Visualize correct");
		this.visualizeFalse = new JButton("Visualize false");
		this.visualizeCorrects.setPreferredSize(this.buttonDimension);
		this.visualizeFalse.setPreferredSize(this.buttonDimension);
		this.visualizeCorrects.setEnabled(false);
		this.visualizeFalse.setEnabled(false);
		this.visualizeCorrects.addActionListener(this.buttonManager);
		this.visualizeFalse.addActionListener(this.buttonManager);
		this.middleEast.add(this.visualizeCorrects);
		this.middleWest.add(this.visualizeFalse);
	}

	/**
	 * Renders testTable, colors correctly classified digts with green, and
	 * falsely classified digts with red
	 * 
	 * @param labels
	 *            labels of dataset
	 * @param data
	 *            inputs of dataset
	 * @param title
	 *            title of jframe
	 * @param correct
	 *            correctness of classification
	 */
	public void showTestTable(String[] labels, String[][] data, String title, boolean[] correct) {
		int cellHeight = 20;
		if (this.t != null)
			if (labels.length != data[0].length) {
				throw new RuntimeException();
			}
		this.t = new JTable(data, labels) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int r, int c) {
				if (r >= data.length && (c == 0 | c == 2)) {
					return true;
				} else {
					return false;
				}
			}
		};
		this.t.setRowHeight(cellHeight);
		StatusColumnCellRenderer sccr = new StatusColumnCellRenderer(correct);
		this.t.getColumnModel().getColumn(1).setCellRenderer(sccr);
		JFrame tableContainer = new JFrame();
		tableContainer.setLocationRelativeTo(this.modegui.centerPane);
		tableContainer.setTitle(title);
		tableContainer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tableContainer.setSize(new Dimension(this.panelWidth / 2, this.panelHeight / 2));
		tableContainer.getContentPane().add(new JScrollPane(this.t), BorderLayout.CENTER);
		tableContainer.pack();
		tableContainer.setVisible(true);

	}

}

/**
 * This class is responsible for coloring correctly classified digits in the
 * table as green cells, and vice versa false cells as red.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
class StatusColumnCellRenderer extends DefaultTableCellRenderer {

	/**
	 * A unique serial version identifier
	 * 
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Correctness of classification
	 */
	boolean[] correct;

	/**
	 * Initializing class fields
	 * 
	 * @param correct
	 *            correctness of classification
	 */
	public StatusColumnCellRenderer(boolean[] correct) {
		this.correct = correct;
	}

	/**
	 * Renders the cell colors.
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int col) {

		// Cells are by default rendered as a JLabel.
		JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		// Get the status for the current row.
		if (correct[row]) {
			l.setBackground(Color.GREEN);
		} else {
			l.setBackground(Color.RED);
		}
		// Return the JLabel which renders the cell.
		return l;

	}
}
