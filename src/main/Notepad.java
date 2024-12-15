package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class Notepad extends JFrame {

	private static final long serialVersionUID = -5768230014485096255L;
	private RSyntaxTextArea textArea;
	private JTextPane lineNumbers;
	private RTextScrollPane scrollPane;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newItem, openItem, saveItem, exitItem;
	private JFileChooser fileChooser;

	public Notepad() {
		setTitle("Notepad");
		setSize(800, 600);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		initComponents();
	}

	private void initComponents() {
		// Text area and line numbers setup
		textArea = new RSyntaxTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        scrollPane = new RTextScrollPane(textArea);
        add(scrollPane);
        
        
		textArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateLineNumbers();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateLineNumbers();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateLineNumbers();
			}
		});

		// Line numbers
		lineNumbers = new JTextPane();
		lineNumbers.setEditable(false);
		lineNumbers.setFont(new Font("Monospaced", Font.PLAIN, 14));
		lineNumbers.setBackground(Color.lightGray);

		// Scroll pane with line numbers
		scrollPane = new RTextScrollPane(textArea);
		scrollPane.setRowHeaderView(lineNumbers);
		add(scrollPane);

		// Menu bar setup
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");

		newItem = new JMenuItem("New");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");

		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);

		setJMenuBar(menuBar);

		// File chooser
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
		fileChooser.setFileFilter(filter);

		// Action listeners for menu items
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});

		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
						textArea.read(reader, null);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
						textArea.write(writer);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	private void updateLineNumbers() {
		lineNumbers.setMargin(new Insets(0, 5, 0, 5));
		String[] lines = textArea.getText().split("\n");
		StringBuilder lineNumberText = new StringBuilder();
		for (int i = 1; i <= lines.length; i++) {
			lineNumberText.append(i).append("\n");
		}
		lineNumbers.setText(lineNumberText.toString());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Notepad().setVisible(true);
			}
		});
	}
}
