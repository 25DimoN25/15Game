import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Main{
	private static JFrame mainFrame,settings;
	private static List<JButton> numbers;
	private static JButton space, winSet[];
	private static int x, y, count = 0;
	
	public static void main(String[] args){
		openSettings();
	}
	
	@SuppressWarnings("serial")
	static void openSettings() {
		
		settings = new JFrame("Настройки") {
			JTextField[] textFields = new JTextField[2];
		{
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(null);
			setResizable(false);
			setBounds(300,300,320,90);
			
			(textFields[0] = new JTextField("кол-во столбцов")).setBounds(100, 15, 100, 30);
			(textFields[1] = new JTextField("кол-во строк")).setBounds(210, 15, 100, 30);
			for (JTextField f : textFields) {

				f.addFocusListener(new FocusAdapter() {public void focusGained(FocusEvent arg0) {f.setText(null);}});
				f.addKeyListener(new KeyAdapter() {
			        public void keyTyped(KeyEvent e) {
				          char a = e.getKeyChar();
				          if (!Character.isDigit(a)) e.consume();
			        }
				});
			}
			
			add(new JButton("Играть"){{
				setBounds(10, 15, 80, 30);
				addActionListener(e -> {
					if (	(((x = Integer.parseInt(textFields[1].getText()))<4)||x>25)||	
							(((y = Integer.parseInt(textFields[0].getText()))<4)||x>25)
						) {
						JOptionPane.showMessageDialog(null, "Введенные размеры должны быть от 4 до 25", "ОШИБКА!", JOptionPane.ERROR_MESSAGE);
					} else {
						settings.setVisible(false);
						initialize(x,y);
					}
					
				});
			}});
			
			for (JTextField f : textFields) add(f);
			setVisible(true);
		}};
	}
	
	static void reset(){
		numbers.forEach(e -> mainFrame.remove(e));
		numbers.forEach(e -> mainFrame.add(e));
		mainFrame.validate();
	}
	
	@SuppressWarnings("serial")
	static void initialize(int x, int y) {
		numbers = new ArrayList<>(x*y);
		
		for (int i = 1; i < x*y; i++) {
			numbers.add(new JButton(new Integer(i).toString()){{
				setFocusable(false);
				setFocusCycleRoot(false);
				addActionListener(button ->{
					int indexCurrent = numbers.indexOf((JButton)button.getSource()),
						indexOfSpace = numbers.indexOf(space);
					if ((Math.abs(indexCurrent-indexOfSpace)==1)||(Math.abs(indexCurrent-indexOfSpace)==y)) {
						mainFrame.setTitle("Кол-во шагов: "+ ++count);
						Collections.swap(numbers, indexCurrent, indexOfSpace);
						reset();
					}
					if (Arrays.equals(numbers.toArray(new JButton[0]), winSet)) win();	
				});
			}});
		}
		numbers.add(space = new JButton("space"){{setVisible(false);}});
		winSet = numbers.toArray(new JButton[0]);
		Collections.shuffle(numbers);
		
		mainFrame = new JFrame(){{
			setBounds(300, 300, 500, 530);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new GridLayout(x,y));
			numbers.forEach(e -> add(e));
			setVisible(true);
		}};
	}

	static void win() {
		
		JOptionPane.showMessageDialog(null, "Поздравляем, вы выиграли за "+count+" шагов", "YOU WIN!", JOptionPane.INFORMATION_MESSAGE); 
		System.exit(0);
	}
}
