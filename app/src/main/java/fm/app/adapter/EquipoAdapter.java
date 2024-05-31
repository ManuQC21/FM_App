package fm.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fm.app.R;
import fm.app.entity.service.Equipo;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder> {

    private List<Equipo> equipos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(int equipoId);
    }

    public EquipoAdapter(List<Equipo> equipos, OnItemClickListener listener) {
        this.equipos = equipos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_equipo, parent, false);
        return new EquipoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipoViewHolder holder, int position) {
        Equipo equipo = equipos.get(position);
        holder.nombreEquipo.setText(equipo.getNombreEquipo());
        holder.tipoEquipo.setText(equipo.getTipoEquipo());
        holder.estadoEquipo.setText(equipo.getEstado());
        holder.editButton.setOnClickListener(v -> listener.onEditClick(equipo.getId()));
    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public static class EquipoViewHolder extends RecyclerView.ViewHolder {
        TextView nombreEquipo, tipoEquipo, estadoEquipo;
        ImageView editButton;

        EquipoViewHolder(View itemView) {
            super(itemView);
            nombreEquipo = itemView.findViewById(R.id.txtNombreEquipo);
            tipoEquipo = itemView.findViewById(R.id.txtTipoEquipo);
            estadoEquipo = itemView.findViewById(R.id.txtEstado);
            editButton = itemView.findViewById(R.id.ic_edit);
        }
    }
    public void updateData(List<Equipo> newEquipos) {
        equipos.clear();
        equipos.addAll(newEquipos);
        notifyDataSetChanged();
    }
}
