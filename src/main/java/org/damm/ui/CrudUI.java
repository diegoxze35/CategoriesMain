package org.damm.ui;

import org.damm.domain.dao.Dao;
import org.damm.domain.pojo.Category;
import org.damm.domain.pojo.Event;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CrudUI extends JFrame {
	
	// DAOs (usamos los mocks, reemplázalos por los tuyos)
	private final Dao<Category, Integer> categoryDao;
	private final Dao<Event, Integer> eventDao;
	
	// Componentes de UI
	private JTextField idCategoryField, nameCategoryField, descriptionCategoryField;
	private JTextField idEventField, nameEventField, descriptionEventField;
	private JSpinner eventDateField;
	private JComboBox<Category> categoryComboBox;
	
	private JTable categoryTable, eventTable;
	private DefaultTableModel categoryTableModel, eventTableModel;
	
	private JTabbedPane tabbedPane;
	
	public CrudUI(Dao<Category, Integer> categoryDao, Dao<Event, Integer> eventDao) {
		this.categoryDao = categoryDao;
		this.eventDao = eventDao;
		setTitle("Gestor de Eventos y Categorías");
		setSize(1000, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));
		
		initComponents();
		addListeners();
		loadInitialData();
	}
	
	private void initComponents() {
		// Panel de Formularios (Centro, pero lo ponemos al norte)
		JPanel formPanel = createFormPanel();
		add(formPanel, BorderLayout.NORTH);
		
		// Panel de Botones (Derecha)
		JPanel buttonPanel = createButtonPanel();
		add(buttonPanel, BorderLayout.EAST);
		
		// Panel de Tablas con Pestañas (Centro)
		tabbedPane = new JTabbedPane();
		
		// Tabla de Categorías
		String[] categoryColumns = {"ID", "Nombre", "Descripción"};
		categoryTableModel = new DefaultTableModel(categoryColumns, 0);
		categoryTable = new JTable(categoryTableModel);
		tabbedPane.addTab("Categorías", new JScrollPane(categoryTable));
		
		// Tabla de Eventos
		String[] eventColumns = {"ID", "Nombre", "Descripción", "Fecha", "Categoría"};
		eventTableModel = new DefaultTableModel(eventColumns, 0);
		eventTable = new JTable(eventTableModel);
		tabbedPane.addTab("Eventos", new JScrollPane(eventTable));
		
		add(tabbedPane, BorderLayout.CENTER);
	}
	
	private JPanel createFormPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Panel de Categoría
		JPanel categoryPanel = new JPanel(new GridBagLayout());
		categoryPanel.setBorder(BorderFactory.createTitledBorder("Datos de Categoría"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		categoryPanel.add(new JLabel("ID Categoría:"), gbc);
		gbc.gridx = 1;
		idCategoryField = new JTextField(10);
		idCategoryField.setEditable(false);
		categoryPanel.add(idCategoryField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		categoryPanel.add(new JLabel("Nombre:"), gbc);
		gbc.gridx = 1;
		nameCategoryField = new JTextField(20);
		categoryPanel.add(nameCategoryField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		categoryPanel.add(new JLabel("Descripción:"), gbc);
		gbc.gridx = 1;
		descriptionCategoryField = new JTextField(30);
		categoryPanel.add(descriptionCategoryField, gbc);
		
		// Panel de Evento
		JPanel eventPanel = new JPanel(new GridBagLayout());
		eventPanel.setBorder(BorderFactory.createTitledBorder("Datos de Evento"));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		eventPanel.add(new JLabel("ID Evento:"), gbc);
		gbc.gridx = 1;
		idEventField = new JTextField(10);
		idEventField.setEditable(false);
		eventPanel.add(idEventField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		eventPanel.add(new JLabel("Nombre:"), gbc);
		gbc.gridx = 1;
		nameEventField = new JTextField(20);
		eventPanel.add(nameEventField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		eventPanel.add(new JLabel("Descripción:"), gbc);
		gbc.gridx = 1;
		descriptionEventField = new JTextField(30);
		eventPanel.add(descriptionEventField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		eventPanel.add(new JLabel("Fecha:"), gbc);
		gbc.gridx = 1;
		eventDateField = new JSpinner(new SpinnerDateModel());
		eventPanel.add(eventDateField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		eventPanel.add(new JLabel("Categoría:"), gbc);
		gbc.gridx = 1;
		categoryComboBox = new JComboBox<>();
		eventPanel.add(categoryComboBox, gbc);
		
		mainPanel.add(categoryPanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(eventPanel);
		
		return mainPanel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 1, 10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JButton selectButton = new JButton("Seleccionar por ID");
		JButton selectAllButton = new JButton("Seleccionar Todo");
		JButton createButton = new JButton("Crear");
		JButton updateButton = new JButton("Actualizar");
		JButton deleteButton = new JButton("Eliminar");
		JButton clearButton = new JButton("Limpiar Campos");
		
		panel.add(selectButton);
		panel.add(selectAllButton);
		panel.add(createButton);
		panel.add(updateButton);
		panel.add(deleteButton);
		panel.add(clearButton);
		
		// Action Listeners para los botones
		selectButton.addActionListener(e -> findById());
		selectAllButton.addActionListener(e -> findAll());
		createButton.addActionListener(e -> createEntity());
		updateButton.addActionListener(e -> updateEntity());
		deleteButton.addActionListener(e -> deleteEntity());
		clearButton.addActionListener(e -> clearFields());
		
		return panel;
	}
	
	private void addListeners() {
		// Listener para la tabla de categorías
		categoryTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && categoryTable.getSelectedRow() != -1) {
				int selectedRow = categoryTable.getSelectedRow();
				idCategoryField.setText(categoryTableModel.getValueAt(selectedRow, 0).toString());
				nameCategoryField.setText(categoryTableModel.getValueAt(selectedRow, 1).toString());
				descriptionCategoryField.setText(categoryTableModel.getValueAt(selectedRow, 2).toString());
			}
		});
		
		// Listener para la tabla de eventos
		eventTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && eventTable.getSelectedRow() != -1) {
				int selectedRow = eventTable.getSelectedRow();
				idEventField.setText(eventTableModel.getValueAt(selectedRow, 0).toString());
				nameEventField.setText(eventTableModel.getValueAt(selectedRow, 1).toString());
				descriptionEventField.setText(eventTableModel.getValueAt(selectedRow, 2).toString());
				
				// Convertir String de fecha a Date
				try {
					Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(eventTableModel.getValueAt(selectedRow, 3).toString());
					eventDateField.setValue(date);
				} catch (ParseException ex) {
					eventDateField.setValue(new Date());
				}
				
				// Seleccionar la categoría en el ComboBox
				String categoryName = eventTableModel.getValueAt(selectedRow, 4).toString();
				for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
					if (categoryComboBox.getItemAt(i).getName().equals(categoryName)) {
						categoryComboBox.setSelectedIndex(i);
						break;
					}
				}
			}
		});
	}
	
	private void loadInitialData() {
		refreshCategoryComboBox();
		refreshCategoryTable();
		refreshEventTable();
	}
	
	// --- Métodos de Lógica CRUD ---
	
	private void findById() {
		if (isCategoryTabSelected()) {
			try {
				int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese ID de Categoría:"));
				Category category = categoryDao.findById(id);
				categoryTableModel.setRowCount(0); // Limpiar tabla
				if (category != null) {
					categoryTableModel.addRow(new Object[]{category.getIdCategory(), category.getName(), category.getDescription()});
				} else {
					JOptionPane.showMessageDialog(this, "Categoría no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			// Lógica para buscar Evento por ID
			try {
				int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese ID de Evento:"));
				Event event = eventDao.findById(id);
				eventTableModel.setRowCount(0); // Limpiar tabla
				if (event != null) {
					addEventToTable(event);
				} else {
					JOptionPane.showMessageDialog(this, "Evento no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void findAll() {
		if (isCategoryTabSelected()) {
			refreshCategoryTable();
		} else {
			refreshEventTable();
		}
	}
	
	private void createEntity() {
		if (isCategoryTabSelected()) {
			String name = nameCategoryField.getText();
			String desc = descriptionCategoryField.getText();
			if (name.isEmpty()) {
				JOptionPane.showMessageDialog(this, "El nombre de la categoría es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Category newCategory = new Category(null, name, desc);
			categoryDao.save(newCategory);
			refreshCategoryTable();
			refreshCategoryComboBox();
			clearFields();
		} else {
			String name = nameEventField.getText();
			String desc = descriptionEventField.getText();
			Date date = (Date) eventDateField.getValue();
			Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
			
			if (name.isEmpty() || selectedCategory == null) {
				JOptionPane.showMessageDialog(this, "Nombre y categoría son obligatorios para el evento.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Event newEvent = new Event(null, desc, date, name, selectedCategory);
			eventDao.save(newEvent);
			refreshEventTable();
			clearFields();
		}
	}
	
	private void updateEntity() {
		if (isCategoryTabSelected()) {
			try {
				int id = Integer.parseInt(idCategoryField.getText());
				String name = nameCategoryField.getText();
				String desc = descriptionCategoryField.getText();
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(this, "El nombre de la categoría es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Category categoryToUpdate = new Category(id, name, desc);
				categoryDao.save(categoryToUpdate);
				refreshCategoryTable();
				refreshCategoryComboBox();
				clearFields();
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Seleccione una categoría de la tabla para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			try {
				int id = Integer.parseInt(idEventField.getText());
				String name = nameEventField.getText();
				String desc = descriptionEventField.getText();
				Date date = (Date) eventDateField.getValue();
				Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
				
				if (name.isEmpty() || selectedCategory == null) {
					JOptionPane.showMessageDialog(this, "Nombre y categoría son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Event eventToUpdate = new Event(id, desc, date, name, selectedCategory);
				eventDao.save(eventToUpdate);
				refreshEventTable();
				clearFields();
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Seleccione un evento de la tabla para actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void deleteEntity() {
		if (isCategoryTabSelected()) {
			try {
				int id = Integer.parseInt(idCategoryField.getText());
				int choice = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar esta categoría? Esto podría afectar a los eventos asociados.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					Category categoryToDelete = new Category();
					categoryToDelete.setIdCategory(id);
					categoryDao.delete(categoryToDelete);
					refreshCategoryTable();
					refreshCategoryComboBox();
					clearFields();
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Seleccione una categoría de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			try {
				int id = Integer.parseInt(idEventField.getText());
				int choice = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar este evento?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					Event eventToDelete = new Event();
					eventToDelete.setIdEvent(id);
					eventDao.delete(eventToDelete);
					refreshEventTable();
					clearFields();
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Seleccione un evento de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	// --- Métodos de Ayuda ---
	private void refreshCategoryTable() {
		categoryTableModel.setRowCount(0); // Limpiar
		List<Category> categories = categoryDao.findAll();
		for (Category category : categories) {
			categoryTableModel.addRow(new Object[]{category.getIdCategory(), category.getName(), category.getDescription()});
		}
	}
	
	private void refreshEventTable() {
		eventTableModel.setRowCount(0); // Limpiar
		List<Event> events = eventDao.findAll();
		for (Event event : events) {
			addEventToTable(event);
		}
	}
	
	private void addEventToTable(Event event) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		eventTableModel.addRow(new Object[]{
				event.getIdEvent(),
				event.getName(),
				event.getDescription(),
				sdf.format(event.getEventDate()),
				event.getCategory() != null ? event.getCategory().getName() : "N/A"
		});
	}
	
	private void refreshCategoryComboBox() {
		categoryComboBox.removeAllItems();
		List<Category> categories = categoryDao.findAll();
		for (Category category : categories) {
			categoryComboBox.addItem(category);
		}
	}
	
	private void clearFields() {
		idCategoryField.setText("");
		nameCategoryField.setText("");
		descriptionCategoryField.setText("");
		idEventField.setText("");
		nameEventField.setText("");
		descriptionEventField.setText("");
		eventDateField.setValue(new Date());
		categoryComboBox.setSelectedIndex(-1);
		categoryTable.clearSelection();
		eventTable.clearSelection();
	}
	
	private boolean isCategoryTabSelected() {
		return tabbedPane.getSelectedIndex() == 0;
	}
}