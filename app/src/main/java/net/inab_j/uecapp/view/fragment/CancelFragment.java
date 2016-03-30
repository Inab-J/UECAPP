package net.inab_j.uecapp.view.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.inab_j.uecapp.R;
import net.inab_j.uecapp.controller.provider.GetCancelTask;
import net.inab_j.uecapp.view.adapter.CancelAdapter;
import net.inab_j.uecapp.view.widget.CalendarItem;
import net.inab_j.uecapp.view.widget.CancelItem;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View mView;

    public CancelFragment() {}

    /**
     * ファクトリメソッド
     * @return A new instance of fragment CancelFragment.
     */
    public static CancelFragment newInstance() {
        CancelFragment fragment = new CancelFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cancel, container, false);

        CancelAdapter adapter = new CancelAdapter(getActivity().getApplicationContext());
        ListView lv = (ListView) mView.findViewById(R.id.content_list_view);
        lv.setOnItemClickListener(this);
        GetCancelTask task = new GetCancelTask(adapter, lv, this);
        task.execute();

        return mView;
    }

    /**
     * 休講情報がなかった場合、GetCancelTaskから呼び出して、メッセージを表示する。
     */
    public void setVisible() {
        mView.findViewById(R.id.nolist).setVisibility(View.VISIBLE);
    }

    public void showProgress() {
        getActivity().findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        getActivity().findViewById(R.id.progressbar).setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CancelItem item = (CancelItem) parent.getItemAtPosition(position);
        DetailDialog remarkDialog = DetailDialog.newInstance(
                item.getDate() + "　" + item.getPeriod() + "時限",
                item.getClassname(), item.getSubject(), item.getNote()
        );
        remarkDialog.show(getFragmentManager(), "remark");
    }

    public static class DetailDialog extends DialogFragment {

        public static DetailDialog newInstance(
                String date, String classname, String subject, String note) {
            DetailDialog fragment = new DetailDialog();
            Bundle args = new Bundle();
            args.putString("date", date);
            args.putString("classname", classname);
            args.putString("subject", subject);
            args.putString("note", note);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String date = getArguments().getString("date");
            String classname = getArguments().getString("classname");
            String subject = getArguments().getString("subject");
            String note = getArguments().getString("note");

            LayoutInflater inflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View content = inflater.inflate(R.layout.dialog_cancel_detail, null);

            ((TextView) content.findViewById(R.id.cancel_date)).setText(date);
            ((TextView) content.findViewById(R.id.cancel_classname)).setText(classname);
            if (note.length() > 0) {
                ((TextView) content.findViewById(R.id.cancel_note)).setText(note);
                (content.findViewById(R.id.cancel_note_container)).setVisibility(View.VISIBLE);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(subject);
            builder.setView(content);
            return builder.create();
        }
    }
}
