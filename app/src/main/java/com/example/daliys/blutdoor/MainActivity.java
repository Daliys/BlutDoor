package com.example.daliys.blutdoor;

import android.app.Activity;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import android.os.Bundle;
import android.util.Log;
import java.util.UUID;

public class MainActivity extends Activity {

    private BluetoothAdapter Bluetooth = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket = null;
    private String MacAddress = "98:D3:32:20:96:C3";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializationBlutooth();

    }

    private void InitializationBlutooth(){
        if(Bluetooth == null){      //Обработка событий при отсуствии Bluetooth или его необнаружении

            AlertDialog.Builder buider = new AlertDialog.Builder(MainActivity.this);
            buider.setTitle("Критическая ошибка")
                    .setCancelable(false)
                    .setMessage("Отсуствие Bluetooth или ошибки при его обнаружении и подключении.\n(Приложении будет закрыто)")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    finish();
                }
            });
            buider.show();
        }else {         //если блютуз есть то продолжать

            long StartTimer = System.currentTimeMillis();
            long PastTime = 0;
            while ((!Bluetooth.isEnabled()) && (PastTime < 5000)) {    //попытка включить блютуз в течении 5 сек
                PastTime = System.currentTimeMillis() - StartTimer;
                Bluetooth.enable();
                try {
                    Thread.currentThread().sleep(950);
                } catch (Exception e) {
                }
            }
            if (!Bluetooth.isEnabled()) {    //если блютуз после 5 сек не включился то закрыть приложение
                AlertDialog.Builder buider = new AlertDialog.Builder(MainActivity.this);
                buider.setTitle("Критическая ошибка")
                        .setCancelable(false)
                        .setMessage("Ошибка при включении Bluetooth.\n(Приложении будет закрыто)")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
                buider.show();
            } else {           //если блютуз включился то продолжать

                try {
                    BluetoothDevice device = Bluetooth.getRemoteDevice(MacAddress);
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    Bluetooth.cancelDiscovery();
                    btSocket.connect();
                } catch (Exception e) {
                    Log.e("Bluetooth", "Eroor Connect socket to Bluetooth");
                    AlertDialog.Builder buider = new AlertDialog.Builder(MainActivity.this);
                    buider.setTitle("Критическая ошибка")
                            .setCancelable(false)
                            .setMessage("Ошибка при подключении к Bluetooth. Проверте доступность Bluetooth.\n(Приложении будет закрыто)")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    buider.show();
                }


            }
        }

    }

}
