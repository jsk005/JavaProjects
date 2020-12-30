package com.link2me.android.recyclerview.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.link2me.android.recyclerview.R;
import com.link2me.android.recyclerview.model.Address_Item;
import com.link2me.android.recyclerview.model.RetrofitUrl;

import java.util.List;

import static com.link2me.android.recyclerview.fragment.RecyclerViewFragment.isCheckFlag;
import static com.link2me.android.recyclerview.fragment.RecyclerViewFragment.fragconstraintLayout;

public class FragmentRecyclerViewAdapter extends RecyclerView.Adapter<FragmentRecyclerViewAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    private List<Address_Item> rvItemList;

    // 인터페이스 선언 -------------------------------------------------------------
    private OnItemClickListener fListener;

    public interface OnItemClickListener {
        void onItemClicked(View view, Address_Item item, int position);
    }

    public void setOnItemChoiceClickListener(OnItemClickListener listener){
        fListener = listener;
    }
    // 인터페이스 ----------------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout child_layout;
        ImageView photo_Image;
        TextView tv_name;
        TextView tv_mobileNO;
        TextView tv_officeNO;
        ImageView call_btn;
        CheckBox cbSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            child_layout = itemView.findViewById(R.id.child_layout);
            photo_Image = itemView.findViewById(R.id.profile_Image);
            tv_name = itemView.findViewById(R.id.child_name);
            tv_mobileNO = itemView.findViewById(R.id.child_mobileNO);
            tv_officeNO = itemView.findViewById(R.id.child_officeNO);
            call_btn = itemView.findViewById(R.id.call_btn);
            cbSelect = itemView.findViewById(R.id.listcell_checkbox);
        }
    }

    public FragmentRecyclerViewAdapter(Context context, List<Address_Item> itemList) {
        this.context = context;
        rvItemList = itemList;
    }

    public void selectAll(){ // checkbox 전체 선택
        for(int i=0;i < rvItemList.size();i++){
            rvItemList.get(i).setCheckBoxState(true);
        }
    }
    public void unselectall(){ // checkbox 전체 해제
        for(int i=0;i < rvItemList.size();i++){
            rvItemList.get(i).setCheckBoxState(false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // UI 생성 및 초기화
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e(TAG,"onBindViewHolder: called."); // 실제로 데이터를 표시하는 부분
        Address_Item currentItem = rvItemList.get(position);

        String photoURL = RetrofitUrl.BASE_URL + "photos/" + currentItem.getPhoto();
        if(photoURL.contains("null")){
            Glide.with(context).asBitmap().load(R.drawable.photo_base).into(holder.photo_Image);
        } else {
            Glide.with(context).asBitmap().load(photoURL).into(holder.photo_Image);
        }
        holder.tv_name.setText(currentItem.getUserNM());
        holder.tv_mobileNO.setText(PhoneNumberUtils.formatNumber(currentItem.getMobileNO()));
        holder.tv_officeNO.setText(PhoneNumberUtils.formatNumber(currentItem.getTelNO()));

        holder.photo_Image.setOnClickListener(view1 -> {
            if(fListener != null){
                fListener.onItemClicked(view1,currentItem,position);
                // 클릭의 결과로 값을 전달
                Log.e(TAG,"item clicked : "+position);
            }
        });

        holder.call_btn.setOnClickListener(view -> {
            Toast.makeText(context, "전화걸기 또는 문자 보내기를 할 수 있어요.", Toast.LENGTH_SHORT).show();
        });

        final String[] items ={"휴대폰 전화걸기","사무실전화 걸기"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("해당작업을 선택하세요");
        builder.setItems(items, (dialog, which) -> {
            Toast.makeText(context, items[which] + "선택했습니다.", Toast.LENGTH_SHORT).show();
            switch (which){
                case 0:
                    if(currentItem.getMobileNO().length() ==0){
                        Toast.makeText(context, "전화걸 휴대폰 번호가 없습니다.",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    AlertDialog dialog1 = new AlertDialog.Builder(context)
                            .setTitle(currentItem.getUserNM())
                            .setMessage(PhoneNumberUtils.formatNumber(currentItem.getMobileNO()) + " 통화하시겠습니까?")
                            .setPositiveButton("예",
                                    (dialog23, which13) -> {

                                        Intent intent = new Intent(Intent.ACTION_CALL,
                                                Uri.parse("tel:" + PhoneNumberUtils.formatNumber(currentItem.getMobileNO())));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    })
                            .setNegativeButton(
                                    "아니오",
                                    (dialog22, which12) -> dialog22.dismiss()).create();
                    dialog1.show();
                    break;
                case 1:
                    if(currentItem.getTelNO().length() ==0){
                        Toast.makeText(context, "전화걸 사무실 번호가 없습니다.",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    AlertDialog dialog2 = new AlertDialog.Builder(context)
                            .setTitle(currentItem.getUserNM())
                            .setMessage(PhoneNumberUtils.formatNumber(currentItem.getTelNO()) + " 통화하시겠습니까?")
                            .setPositiveButton("예",
                                    (dialog3, which1) -> {

                                        Intent intent = new Intent(Intent.ACTION_CALL,
                                                Uri.parse("tel:" + PhoneNumberUtils.formatNumber(currentItem.getTelNO())));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    })
                            .setNegativeButton(
                                    "아니오",
                                    (dialog32, which14) -> dialog32.dismiss()).create();
                    dialog2.show();
                    break;

            }
        });
        builder.create();

        holder.call_btn.setOnClickListener(view -> builder.show());

        if (isCheckFlag == false) {
            holder.call_btn.setVisibility(View.VISIBLE);
            holder.call_btn.setOnClickListener(view -> builder.show());
            holder.cbSelect.setVisibility(View.GONE);
            holder.child_layout.setOnClickListener(view ->
                    Toast.makeText(context, "상세보기를 눌렀습니다 ===" + currentItem.getIdx(), Toast.LENGTH_SHORT).show());

            holder.child_layout.setOnLongClickListener(v -> {
                isCheckFlag = true;
                fragconstraintLayout.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
                return true;
            });
        } else {
            holder.call_btn.setVisibility(View.GONE);
            //convertView.setClickable(false);
            holder.cbSelect.setVisibility(View.VISIBLE);
            holder.cbSelect.setTag(position); // This line is important.

            // 체크 박스 클릭하면 CheckBoxState 에 반영한다. setOnCheckedChangeListener 대신 사용
            holder.cbSelect.setOnClickListener(v -> {
                if(rvItemList.get(position).getCheckBoxState() == true){
                    rvItemList.get(position).setCheckBoxState(false);
                    Log.d("checkbox","position : "+ position + " checkBoxState === "+ rvItemList.get(position).getCheckBoxState());
                } else {
                    rvItemList.get(position).setCheckBoxState(true);
                    Log.d("checkbox","position : "+ position + " checkBoxState === "+ rvItemList.get(position).getCheckBoxState());
                }
            });

            holder.child_layout.setOnClickListener(v -> {
                if(holder.cbSelect.isChecked() == false){
                    holder.cbSelect.setChecked(true);
                    rvItemList.get(position).setCheckBoxState(true);
                    //notifyDataSetChanged();
                    Log.d("checklist","position : "+ position + " checkBoxState === "+ rvItemList.get(position).getCheckBoxState());
                } else {
                    holder.cbSelect.setChecked(false);
                    rvItemList.get(position).setCheckBoxState(false);
                    //notifyDataSetChanged();
                    Log.d("checklist","position : "+ position + " checkBoxState === "+ rvItemList.get(position).getCheckBoxState());
                }
            });
        }

        // 재사용 문제 해결
        if(rvItemList.get(position).getCheckBoxState() == true){
            holder.cbSelect.setChecked(true);
            Log.d("ReUse","position : " + position + " checkBoxState === " + rvItemList.get(position).getCheckBoxState());
        } else {
            holder.cbSelect.setChecked(false);
            Log.d("ReUse","position : "+position + " checkBoxState ===" + rvItemList.get(position).getCheckBoxState());
        }

    }

    @Override
    public int getItemCount() {
        return rvItemList.size();
    }


}


