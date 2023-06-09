package com.example.trabajofinal_mobiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {

    EditText etCantidad, etPrecioPersona;

    CheckBox checkIva, checkDescuento;

    TextView tvIva, tvDescuento, tvTotalBruto, tvTotalIva, tvTotalDescuento, tvTotalNeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Asociación de las variables con los elementos XML
        etCantidad = findViewById(R.id.etCantidad);
        etPrecioPersona = findViewById(R.id.etPrecioPersona);

        checkIva = findViewById(R.id.checkIva);
        checkDescuento = findViewById(R.id.checkDescuento);

        tvIva = findViewById(R.id.tvIva);
        tvDescuento = findViewById(R.id.tvDescuento);
        tvTotalBruto = findViewById(R.id.tvTotalBruto);
        tvTotalIva = findViewById(R.id.tvTotalIva);
        tvTotalDescuento = findViewById(R.id.tvTotalDescuento);
        tvTotalNeto = findViewById(R.id.tvTotalNeto);

    }

    public void Calcular(View view){
        String cantidad = etCantidad.getText().toString();
        String precioPersona = etPrecioPersona.getText().toString();
        if (cantidad.isEmpty() || precioPersona.isEmpty()){
            Toast.makeText(this, "Campos Obligatorios!", Toast.LENGTH_SHORT).show();
        }else{
            int intCantidad = Integer.parseInt(cantidad);
            float floatPrecioPersona = Float.parseFloat(precioPersona);

            Cartagena cartagena = new Cartagena(intCantidad, floatPrecioPersona);

            if (checkIva.isChecked()){
                final int IVA = 19;
                cartagena.setIva(IVA);
                tvIva.setText("19%");
            }else{
                tvIva.setText("");
            }
            if( checkDescuento.isChecked()){
                final int DESCUENTO = 10;
                cartagena.setDescuento(DESCUENTO);
                tvDescuento.setText("10%");
            }else{
                tvDescuento.setText("");
            }

            float TotalBruto = cartagena.totalBruto();
            tvTotalBruto.setText(String.valueOf(TotalBruto));

            float TotalIva = cartagena.totalIva();
            tvTotalIva.setText(String.valueOf(TotalIva));

            float TotalDescuento = cartagena.totalDescuento();
            tvTotalDescuento.setText(String.valueOf(TotalDescuento));

            float TotalNeto = cartagena.totalNeto();
            tvTotalNeto.setText(String.valueOf(TotalNeto));
        }
    }

    public void Limpiar(View view){
        etCantidad.setText("");
        etPrecioPersona.setText("");
        tvTotalBruto.setText("");
        tvTotalIva.setText("");
        tvTotalDescuento.setText("");
        tvTotalNeto.setText("");
        etCantidad.requestFocus();
        checkIva.setChecked(false);
        checkDescuento.setChecked(false);
    }

    public void Regresar(View view){
        Intent regresar = new Intent(this, MainActivity2.class);
        startActivity(regresar);
    }
}