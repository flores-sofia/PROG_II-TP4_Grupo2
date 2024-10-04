package com.example.tp4_grupo2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tp4_grupo2.Categoria;
import com.example.tp4_grupo2.R;

import java.util.List;

public class CategoriaAdapter extends ArrayAdapter<Categoria> {

    public CategoriaAdapter(Context context, List<Categoria> objetos) {
        super(context, R.layout.spinner_item_template, objetos);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.spinner_item_template, parent, false);

        TextView tvDescripcion = item.findViewById(R.id.descripcion);

        Categoria categoria = getItem(position);
        if (categoria != null) {
            tvDescripcion.setText(categoria.getDescripcion());
        }

        return item;
    }
}
