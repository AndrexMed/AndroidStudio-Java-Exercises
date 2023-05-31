package com.example.sobrecarga_exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText Cantidad, ValorUnidad, Descuento;

    TextView tvTitulo, tvTotal;
    RadioGroup radiogrupo, radiogrupo2;

    private int IVA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radiogrupo = findViewById(R.id.radioGrupo1);
        radiogrupo2 = findViewById(R.id.radioGrupo2);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvTotal = findViewById(R.id.tvTotal);

        Cantidad = findViewById(R.id.etCantidad);
        ValorUnidad = findViewById(R.id.etValorUnidad);
        Descuento = findViewById(R.id.etValorDescuento);

        //Se activa cuando se produce un cambio en la selección de los botones de radio dentro del grupo.
        radiogrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            //Este es el método de devolución de llamada que se ejecuta cuando cambia la selección de los botones de radio en el RadioGroup.
            // Recibe como parámetros el RadioGroup y el ID del botón de radio seleccionado.
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Esta funcion convierte el chechedId del boton seleccionado a un string...
                String nombreIdSeleccionadoBTN = getResources().getResourceEntryName(checkedId);

                switch (nombreIdSeleccionadoBTN) {
                    case "btn1":
                        // Acciones para el botón 1 seleccionado
                        IVA = 0;
                        break;
                    case "btn2":
                        // Acciones para el botón 2 seleccionado
                        IVA = 5;
                        break;
                    case "btn3":
                        // Acciones para el botón 3 seleccionado
                        IVA = 10;
                        break;
                    case "btn4":
                        // Acciones para el botón 3 seleccionado
                        IVA = 19;
                        break;
                    default:
                        // Acciones para ningún botón seleccionado
                        
                        break;
                }
            }
        }); //Fin

        radiogrupo2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String nombreIdSeleccionadoBTN2 = getResources().getResourceEntryName(checkedId);

                String stringCantidad = Cantidad.getText().toString();
                String stringValorUnidad = ValorUnidad.getText().toString();
                String stringDescuento = Descuento.getText().toString();

                if (stringCantidad.isEmpty() || stringValorUnidad.isEmpty() || stringDescuento.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Complete los campos!", Toast.LENGTH_SHORT).show();
                } else {

                    int intdescuento = Integer.parseInt(stringDescuento);
                    int intCantidad = Integer.parseInt(stringCantidad);
                    float floatValorUnidad = Float.parseFloat(stringValorUnidad);

                    float totalIva = 0, totalDescuento = 0, totalNeto,totalBruto;

                    ClassMetodos metodo = new ClassMetodos(intCantidad, floatValorUnidad, IVA, intCantidad);

                    switch (nombreIdSeleccionadoBTN2) {

                        case "btnBruto":
                            // Acciones para el botón 1 seleccionado

                            totalBruto = metodo.Calculos(intCantidad, floatValorUnidad);
                            tvTotal.setText(String.valueOf(totalBruto));

                            break;
                        case "btnTotalDescuento":
                            // Acciones para el botón 2 seleccionado
                            totalDescuento = metodo.Calculos(intdescuento);
                            tvTotal.setText(String.valueOf(totalDescuento));
                            break;
                        case "btnTotalIva":
                            // Acciones para el botón 3 seleccionado
                            totalIva = metodo.Calculos(IVA);
                            tvTotal.setText(String.valueOf(totalIva));
                            break;
                        case "btnTotalNeto":
                            // Acciones para el botón 3 seleccionado
                            float ivaAUX, DesAUX;
                            ivaAUX = metodo.Calculos(IVA);
                            DesAUX = metodo.Calculos(intdescuento);
                            totalNeto = metodo.Calculos(ivaAUX, DesAUX);
                            tvTotal.setText(String.valueOf(totalNeto));
                            break;
                        default:
                            // Acciones para ningún botón seleccionado

                            break;
                    }
                }


            }
        });

    }//Fin Oncreate

}