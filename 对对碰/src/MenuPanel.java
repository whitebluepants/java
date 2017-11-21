/**
 * Created by WhiteBlue Pants on 2017/9/11.
 */
import java.awt.*;
import javax.swing.*;

public class MenuPanel extends JPanel
{
    JButton exit,startGame;
    JLabel title;

    MenuPanel()
    {
        setLayout(null);
        exit = new JButton("退出游戏");     //按钮初始化
        exit.setBounds(320,350,100,50);
        exit.setFocusable(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        startGame = new JButton("开始游戏");
        startGame.setBounds(320,250,100,50);
        startGame.setBorderPainted(false);
        startGame.setContentAreaFilled(false);
        startGame.setFocusable(false);

//        title = new JLabel("对对碰");
//        title.setBounds(310,50,300,200);
//        title.setFont(new Font("Dialog",1,40));
//        title.setForeground(Color.red);

        add(exit);
        add(startGame);
        //add(title);
        setOpaque(false);
    }
}
