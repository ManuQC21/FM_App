package fm.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fm.app.R;
import fm.app.entity.service.Equipo;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder> {

    private List<Equipo> equipoList;

    public EquipoAdapter(List<Equipo> equipoList) {
        this.equipoList = equipoList;
    }

    @NonNull
    @Override
    public EquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipo, parent, false);
        return new EquipoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipoViewHolder holder, int position) {
        Equipo equipo = equipoList.get(position);
        holder.txtNombreEquipo.setText(equipo.getNombreEquipo());
        holder.txtTipoEquipo.setText(equipo.getTipoEquipo());
        holder.txtEstado.setText(equipo.getEstado());
    }

    @Override
    public int getItemCount() {
        return equipoList.size();
    }

    public static class EquipoViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombreEquipo, txtTipoEquipo, txtEstado;

        public EquipoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombreEquipo = itemView.findViewById(R.id.txtNombreEquipo);
            txtTipoEquipo = itemView.findViewById(R.id.txtTipoEquipo);
            txtEstado = itemView.findViewById(R.id.txtEstado);
        }
    }
}
