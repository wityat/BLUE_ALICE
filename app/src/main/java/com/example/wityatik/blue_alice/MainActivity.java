package com.example.wityatik.blue_alice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import static android.R.layout.*;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter bluetoothAdapter;

    ArrayList<String> pairedDeviceArrayList;

    ListView listViewPairedDevice;
    FrameLayout ButPanel,LinePanel,AlwaysPanel;

    ArrayAdapter<String> pairedDeviceAdapter;
    private UUID myUUID;

    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;

    Handler h;
    ImageButton b3,b4,b5,b6,b7,b8;
    Button AllTo,AllFrom,KDplus,KDmines,KPplus,KPmines,calibrationB,setupTurbina,send;
    Switch switch1,switchManual,switchTurbina,SpeedPrint,SensorsPrint;
    EditText text;
   // SeekBar seekBar;


    private StringBuilder sb = new StringBuilder();

    public TextView textInfo, textArdu, d10,textArdu1,textArdu2,textArdu3,d101;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";

        textInfo = (TextView)findViewById(R.id.textInfo);
        textArdu = (TextView)findViewById(R.id.textArdu);
        d10 = (TextView)findViewById(R.id.d10);
        textArdu2 = (TextView)findViewById(R.id.textArdu2);

        b3 = findViewById(R.id.ImageButton3);
        b4 = findViewById(R.id.ImageButton4);
        b5 = findViewById(R.id.ImageButton5);
        b6 = findViewById(R.id.ImageButton6);
        b7 = findViewById(R.id.ImageButton7);
        b8 = findViewById(R.id.ImageButton8);
        calibrationB = findViewById(R.id.calibration);
        AllTo = findViewById(R.id.AllTo);
        AllFrom = findViewById(R.id.AllFrom);
        KDplus = findViewById(R.id.KDplus);
        KDmines = findViewById(R.id.KDmines);
        KPplus = findViewById(R.id.KPplus);
        KPmines = findViewById(R.id.KPmines);
        setupTurbina = findViewById(R.id.setupTurbina);
        send =  findViewById(R.id.send);



        switch1 = findViewById(R.id.switch1);
        switchManual = findViewById(R.id.switchManual);
        switchTurbina = findViewById(R.id.switchTurbina);
        SpeedPrint = findViewById(R.id.SpeedPrint);
        SensorsPrint = findViewById(R.id.SensorsPrint);
     //   seekBar = findViewById(R.id.seekBar);


    /*    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
                if(myThreadConnected!=null) {

                    myThreadConnected.sendData("turbina = "+ progress + ";");

                }

            }

            // Notification that the user has started a touch gesture.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myThreadConnected.sendData("turbina = "+ progress + ";");

            }
        });*/

        text = (EditText)findViewById(R.id.text);
        text.setOnKeyListener(new View.OnKeyListener()
                                  {
                                      public boolean onKey(View v, int keyCode, KeyEvent event)
                                      {
                                          if(event.getAction() == KeyEvent.ACTION_DOWN &&
                                                  (keyCode == KeyEvent.KEYCODE_ENTER))
                                          {
                                              // сохраняем текст, введенный до нажатия Enter в переменную
                                              String strCatName = text.getText().toString();
                                              if(myThreadConnected!=null) {
                                                  myThreadConnected.sendData(strCatName);
                                                  text.setText("");
                                              }
                                              return true;
                                          }
                                          return false;
                                      }
                                  }
        );

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужное сообщение
                              if (isChecked) {
                                  if(myThreadConnected!=null) {
                                      byte[] bytesToSend = "start;".getBytes();
                                      myThreadConnected.write(bytesToSend );
                                             }
                } else {

                            if(myThreadConnected!=null) {
                                byte[] bytesToSend = "stop;".getBytes();
                                myThreadConnected.write(bytesToSend );
                         }
                }
            }
        });
      switchManual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView1, boolean isChecked1) {
                // в зависимости от значения isChecked выводим нужное сообщение
                              if (isChecked1) {
                                  if(myThreadConnected!=null) {
                                      byte[] bytesToSend = "startManualControl;".getBytes();
                                      myThreadConnected.write(bytesToSend );
                                      ButPanel.setVisibility(View.VISIBLE);
                                      LinePanel.setVisibility(View.GONE);
                                             }
                } else {

                            if(myThreadConnected!=null) {
                                byte[] bytesToSend = "stopManualControl;".getBytes();
                                myThreadConnected.write(bytesToSend );
                                ButPanel.setVisibility(View.GONE);
                                LinePanel.setVisibility(View.VISIBLE);

                         }
                }
            }
        });

        SpeedPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView4, boolean isChecked4) {
                // в зависимости от значения isChecked выводим нужное сообщение
                if (isChecked4) {
                    if(myThreadConnected!=null) {
                        byte[] bytesToSend = "start1;".getBytes();
                        myThreadConnected.write(bytesToSend );
                    }
                } else {

                    if(myThreadConnected!=null) {
                        byte[] bytesToSend = "stop1;".getBytes();
                        myThreadConnected.write(bytesToSend );
                    }
                }
            }
        });
        SensorsPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView5, boolean isChecked5) {
                // в зависимости от значения isChecked выводим нужное сообщение
                if (isChecked5) {
                    if(myThreadConnected!=null) {
                        byte[] bytesToSend = "start2;".getBytes();
                        myThreadConnected.write(bytesToSend );
                    }
                } else {

                    if(myThreadConnected!=null) {
                        byte[] bytesToSend = "stop2;".getBytes();
                        myThreadConnected.write(bytesToSend );
                    }
                }
            }
        });
        switchTurbina.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView6, boolean isChecked6) {
                // в зависимости от значения isChecked выводим нужное сообщение
                if (isChecked6) {
                    if(myThreadConnected!=null) {
                        byte[] bytesToSend = "startTurbina;".getBytes();
                        myThreadConnected.write(bytesToSend );
                    }
                } else {

                    if(myThreadConnected!=null) {
                        byte[] bytesToSend = "stopTurbina;".getBytes();
                        myThreadConnected.write(bytesToSend );
                    }
                }
            }
        });



        b3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: onClickBut3(v); break;
                    case MotionEvent.ACTION_UP: Stop(v); break;
                }
                return false;
            }
        });
        b4.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: onClickBut4(v); break;
                    case MotionEvent.ACTION_UP: Stop(v); break;
                }
                return false;
            }
        });
        b5.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: onClickBut5(v); break;
                    case MotionEvent.ACTION_UP: Stop(v); break;
                }
                return false;
            }
        });
        b6.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: onClickBut6(v); break;
                    case MotionEvent.ACTION_UP: Stop(v); break;
                }
                return false;
            }
        });
        b7.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: onClickBut7(v); break;
                    case MotionEvent.ACTION_UP: Stop(v); break;
                }
                return false;
            }
        });
        b8.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: onClickBut8(v); break;
                    case MotionEvent.ACTION_UP: Stop(v); break;
                }
                return false;
            }
        });


        listViewPairedDevice = (ListView)findViewById(R.id.pairedlist);

        ButPanel = (FrameLayout) findViewById(R.id.ButPanel);
        LinePanel = (FrameLayout) findViewById(R.id.LinePanel);
        AlwaysPanel = (FrameLayout) findViewById(R.id.AlwaysPanel);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this, "BLUETOOTH NOT support", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this hardware platform", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        textInfo.setText("Список устройств в радиусе действия:");

    } // END onCreate






    @Override
    protected void onStart() { // Запрос на включение Bluetooth
        super.onStart();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }

    private void setup() { // Создание списка сопряжённых Bluetooth-устройств

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) { // Если есть сопряжённые устройства

            pairedDeviceArrayList = new ArrayList<>();

            for (BluetoothDevice device : pairedDevices) { // Добавляем сопряжённые устройства - Имя + MAC-адресс
                pairedDeviceArrayList.add(device.getName() + "\n" + device.getAddress());
            }

            pairedDeviceAdapter = new ArrayAdapter<>(this, simple_list_item_1, pairedDeviceArrayList);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() { // Клик по нужному устройству

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    listViewPairedDevice.setVisibility(View.GONE); // После клика скрываем список

                         textInfo.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Подключаемся...", Toast.LENGTH_SHORT).show();

                    String  itemValue = (String) listViewPairedDevice.getItemAtPosition(position);
                    String MAC = itemValue.substring(itemValue.length() - 17); // Вычленяем MAC-адрес

                    BluetoothDevice device2 = bluetoothAdapter.getRemoteDevice(MAC);

                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device2);
                    myThreadConnectBTdevice.start();  // Запускаем поток для подключения Bluetooth
                }
            });
        }
    }

    @Override
    protected void onDestroy() { // Закрытие приложения
        super.onDestroy();
        if(myThreadConnectBTdevice!=null) myThreadConnectBTdevice.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){ // Если разрешили включить Bluetooth, тогда void setup()

            if(resultCode == Activity.RESULT_OK) {
                setup();
            }

            else { // Если не разрешили, тогда закрываем приложение

                Toast.makeText(this, "BlueTooth не включён", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private class ThreadConnectBTdevice extends Thread { // Поток для коннекта с Bluetooth

        private BluetoothSocket bluetoothSocket = null;

        private ThreadConnectBTdevice(BluetoothDevice device) {

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() { // Коннект

            boolean success = false;

            try {
                bluetoothSocket.connect();
                success = true;
            }

            catch (IOException e) {
                e.printStackTrace();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Нет коннекта, проверьте Bluetooth-устройство с которым хотите соединиться!", Toast.LENGTH_LONG).show();
                        listViewPairedDevice.setVisibility(View.VISIBLE);
                        textInfo.setVisibility(View.VISIBLE);

                    }
                });

                try {
                    bluetoothSocket.close();
                }

                catch (IOException e1) {

                    e1.printStackTrace();
                }
            }

            if(success) {  // Если законнектились, тогда открываем панель с кнопками и запускаем поток приёма и отправки данных

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        LinePanel.setVisibility(View.VISIBLE); // открываем панель с кнопками
                        AlwaysPanel.setVisibility(View.VISIBLE);
                    }
                });

                myThreadConnected = new ThreadConnected(bluetoothSocket);
                myThreadConnected.start(); // запуск потока приёма и отправки данных
            }
        }


        public void cancel() {

            Toast.makeText(getApplicationContext(), "Close - BluetoothSocket", Toast.LENGTH_LONG).show();

            try {
                bluetoothSocket.close();
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        }

    } // END ThreadConnectBTdevice:



    private class ThreadConnected extends Thread {    // Поток - приём и отправка данных

        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        private String sbprint;

        public ThreadConnected(BluetoothSocket socket) {

            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;
        }


        @Override
        public void run() { // Приём данных

            while (true) {
                try {
                    byte[] buffer = new byte[1];
                    int bytes = connectedInputStream.read(buffer);
                    String strIncom = new String(buffer, 0, bytes);
                    sb.append(strIncom); // собираем символы в строку
                    int endOfLineIndex = sb.indexOf("\r\n"); // определяем конец строки

                    if (endOfLineIndex > 0) {

                        sbprint = sb.substring(0, endOfLineIndex);
                        sb.delete(0, sb.length());



                        runOnUiThread(new Runnable() { // Вывод данных

                            @Override
                            public void run() {

                                switch (sbprint) {

                                    case "robot started":
                                        d10.setText("STARTED");
                                        break;

                                    case "robot stoped":
                                        d10.setText("STOPPED");
                                        break;

                                    case "Manual Control is enabled.":
                                        textArdu2.setText(sbprint);
                                        break;
                                    case "Manual Control is disabled.":
                                        textArdu2.setText(sbprint);
                                        break;

                                    case "Turbina is working.":
                                        textArdu2.setText(sbprint);
                                        break;
                                    case "Turbina is stopped.":
                                        textArdu2.setText(sbprint);
                                        break;
                                    case "Turbina is setuping.":
                                        textArdu2.setText(sbprint);
                                        break;
                                    case "Turbina is setuped.":
                                        textArdu2.setText(sbprint);
                                        break;

                                    default:
                                        textArdu.setText(sbprint);
                                        break;
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }


        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        public void sendData(String message) {
            byte[] msgBuffer = message.getBytes();

            try {
                connectedOutputStream.write(msgBuffer);
            } catch (IOException e) {}
        }

    }

/////////////////// Нажатие кнопок /////////////////////

////////////////////////D11////////////////////////////

    public void onClickBut3(View v) {
        if(myThreadConnected!=null) {

            byte[] bytesToSend = "Back;".getBytes();
            myThreadConnected.write(bytesToSend );
        }

    }public void Stop(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "StopMot;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }


    public void onClickBut4(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "Forward;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    private static long back_pressed;

    @Override
    public void onBackPressed() {
        LinePanel.setVisibility(View.GONE);
        AlwaysPanel.setVisibility(View.GONE);
        ButPanel.setVisibility(View.GONE);
        listViewPairedDevice.setVisibility(View.VISIBLE);
        textInfo.setVisibility(View.VISIBLE);
        if(myThreadConnectBTdevice!=null) myThreadConnectBTdevice.cancel();
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }



//////////////////////D12//////////////////////////

    public void onClickBut5(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "LeftOneWheel;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }


    public void onClickBut6(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "RightOneWheel;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }

    //////////////////////D13//////////////////////////

    public void onClickBut7(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "LeftTwoWheels;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }


    public void onClickBut8(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "RightTwoWheels;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickKPplus(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "KP+0.1;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickKDplus(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "KD+0.05;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickKDmines(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "KD-0.05;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickKPmines(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "KP-0.1;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickAllTo(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "all * toEEPROM;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickAllFrom(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "all * fromEEPROM;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }

        public void onClickCalibrateB(View v) {

        if(myThreadConnected!=null) {

            byte[] bytesToSend = "calibration;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickSetupTurbina(View v) {

        if(myThreadConnected!=null) {
            byte[] bytesToSend = "setupTurbina;".getBytes();
            myThreadConnected.write(bytesToSend );
        }
    }
    public void onClickSend(View v) {

        String strCatName = text.getText().toString();
        text.setText("");
        if(myThreadConnected!=null) {
            myThreadConnected.sendData(strCatName);
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(send.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
} // END
