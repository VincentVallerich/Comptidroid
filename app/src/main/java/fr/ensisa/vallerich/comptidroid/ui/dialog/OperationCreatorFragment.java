package fr.ensisa.vallerich.comptidroid.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;

import fr.ensisa.vallerich.comptidroid.R;
import fr.ensisa.vallerich.comptidroid.database.AppDatabase;
import fr.ensisa.vallerich.comptidroid.databinding.OperationCreatorFragmentBinding;
import fr.ensisa.vallerich.comptidroid.ui.account.AccountFragmentArgs;
import fr.ensisa.vallerich.comptidroid.ui.operation.OperationViewModel;
import fr.ensisa.vallerich.comptidroid.ui.picker.DatePickerFragment;

public class OperationCreatorFragment extends DialogFragment implements DialogInterface.OnDismissListener {

    private static final String OPERATION_DATE = "operation_date";
    private static final String VALUE_DATE = "value_date";
    private OperationCreatorFragmentBinding binding;
    private OperationViewModel mViewModel;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OperationViewModel.class);
        mViewModel.setOperationDao(AppDatabase.get().getOperationDao());
        mViewModel.createOperation();
        binding = OperationCreatorFragmentBinding.inflate(LayoutInflater.from(getContext()));
        binding.setVm(mViewModel);

        MaterialAlertDialogBuilder b = new MaterialAlertDialogBuilder(getContext());
        b.setTitle(R.string.new_operation);
        b.setView(binding.getRoot());
        b.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mViewModel.save();
                dismiss();
            }
        });

        binding.operationDate.setOnClickListener(operationDateListener());
        binding.textOperationDate.setOnClickListener(operationDateListener());

        binding.valueDate.setOnClickListener(valueDateListener());
        binding.textValueDate.setOnClickListener(valueDateListener());

        return b.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel.getOperation().observe(getViewLifecycleOwner(), o -> binding.id.setText((int) o.getOid()));
        mViewModel.getOperationDate().observe(getViewLifecycleOwner(), d -> binding.operationDate.setText(d.toString()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener(OPERATION_DATE, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(OPERATION_DATE)) {
                    Date date = new Date(result.getLong(DatePickerFragment.DATE));
                    mViewModel.setOperationDate(date);
                }
            }
        });
    }

    private View.OnClickListener operationDateListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OperationCreatorFragmentDirections.ActionOperationToDatePicker action = OperationCreatorFragmentDirections.actionOperationToDatePicker();
                action.setTitle(R.string.operationDate);
                action.setDate(new Date().getTime());
                action.setRequestKey(OPERATION_DATE);
                NavHostFragment.findNavController(OperationCreatorFragment.this).navigate(action);
            }
        };
    }

    private View.OnClickListener valueDateListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OperationCreatorFragmentDirections.ActionOperationToDatePicker action = OperationCreatorFragmentDirections.actionOperationToDatePicker();
                action.setTitle(R.string.valueDate);
                action.setDate(new Date().getTime());
                action.setRequestKey(VALUE_DATE);
                NavHostFragment.findNavController(OperationCreatorFragment.this).navigate(action);
            }
        };
    }
}
