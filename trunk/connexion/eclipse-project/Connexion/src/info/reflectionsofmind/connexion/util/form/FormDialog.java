package info.reflectionsofmind.connexion.util.form;

import info.reflectionsofmind.connexion.util.Util;
import info.reflectionsofmind.connexion.util.form.Form.Field;
import info.reflectionsofmind.connexion.util.form.Form.IntField;
import info.reflectionsofmind.connexion.util.form.Form.StringField;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public abstract class FormDialog extends JDialog
{
	private final Form form;
	private final List<JTextField> textFields = new ArrayList<JTextField>();
	private boolean cancelled = false;

	public FormDialog(JFrame parent, Form form, String title, String submitActionName)
	{
		super(parent, title, true);
		this.form = form;

		setLayout(new MigLayout("", "[]6[30][grow]", Util.copy(form.getFields().size(), "[]6") + "[]"));

		final SubmitAction submitAction = new SubmitAction(submitActionName);

		for (Field<?> field : form.getFields())
		{
			add(new JLabel(field.getTitle()), "grow");

			if (field instanceof IntField)
			{
				final JTextField textField = new JTextField(((IntField) field).getValue().toString());
				textField.setSelectionStart(0);
				textField.setSelectionEnd(textField.getText().length());
				textField.setAction(submitAction);

				add(textField, "grow, wrap");
				textFields.add(textField);
			}
			else if (field instanceof StringField)
			{
				final JTextField textField = new JTextField(((StringField) field).getValue());
				textField.setAction(submitAction);

				add(textField, "grow, span");
				textFields.add(textField);
				break;
			}
			else
			{
				throw new RuntimeException("Unknown field class [" + field.getClass() + "]");
			}
		}

		add(new JButton(submitAction), "span, split, sg, right");
		add(new JButton(new CancelAction("Cancel")), "sg, right");

		pack();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
	}
	public Form getForm()
	{
		return this.form;
	}

	// TODO Validation

	private void submitAndDispose()
	{
		Iterator<JTextField> textFieldIterator = textFields.iterator();
		Iterator<Field<?>> formFieldIterator = form.getFields().iterator();

		while (textFieldIterator.hasNext() && formFieldIterator.hasNext())
		{
			Field<?> formField = formFieldIterator.next();
			JTextField textField = textFieldIterator.next();

			if (formField instanceof StringField)
			{
				((StringField) formField).setValue(textField.getText());
				break;
			}
			else if (formField instanceof IntField)
			{
				((IntField) formField).setValue(Integer.valueOf(textField.getText()));
				break;
			}
			else
			{
				throw new RuntimeException("Unknown field class [" + formField.getClass() + "]");
			}
		}

		dispose();
		onSubmit();
	}

	protected abstract void onSubmit();

	private void cancelAndDispose()
	{
		this.cancelled = true;
		dispose();
		onCancel();
	}

	protected void onCancel()
	{
	}

	public boolean isCancelled()
	{
		return this.cancelled;
	}

	@Override
	protected void processWindowEvent(WindowEvent e)
	{
		super.processWindowEvent(e);

		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			cancelAndDispose();
		}
	}

	private class SubmitAction extends AbstractAction
	{
		public SubmitAction(String actionName)
		{
			super(actionName);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			submitAndDispose();
		}
	}

	private class CancelAction extends AbstractAction
	{
		public CancelAction(String actionName)
		{
			super(actionName);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			cancelAndDispose();
		}
	}
}
