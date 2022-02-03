package fr.ensisa.vallerich.comptidroid.ui.operation;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.ensisa.vallerich.comptidroid.R;
import fr.ensisa.vallerich.comptidroid.database.AppDatabase;
import fr.ensisa.vallerich.comptidroid.databinding.OperationCreatorFragmentBinding;
import fr.ensisa.vallerich.comptidroid.model.Type;
import fr.ensisa.vallerich.comptidroid.ui.picker.DatePickerFragment;

public class OperationCreatorFragment extends DialogFragment implements DialogInterface.OnDismissListener {
    public static final String ID = "operationId";
    private static final String OPERATION_DATE = "operation_date";
    private static final String VALUE_DATE = "value_date";
    private OperationCreatorFragmentBinding binding;
    private OperationViewModel mViewModel;

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

        getParentFragmentManager().setFragmentResultListener(VALUE_DATE, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(VALUE_DATE)) {
                    Date date = new Date(result.getLong(DatePickerFragment.DATE));
                    mViewModel.setValueDate(date);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = OperationCreatorFragmentBinding.inflate(LayoutInflater.from(getContext()));

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.deleteCurrentOperation();
                dismiss();
            }
        });

        binding.create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHideSoftKeyboard(view);
                long operationId = mViewModel.save();
                Bundle result = new Bundle();
                result.putLong(ID, operationId);
                String requestKey = OperationCreatorFragmentArgs.fromBundle(getArguments()).getRequestKey();
                getParentFragmentManager().setFragmentResult(requestKey, result);
                dismiss();
            }
        });

        binding.setSetAmount(new TextViewBindingAdapter.OnTextChanged() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) return;
                BigDecimal amount = new BigDecimal(s.toString());
                if (amount.floatValue() > mViewModel.getMaxAmount().getValue().floatValue()
                        && Type.DEBIT.equals(mViewModel.getType().getValue())) {
                    binding.amountError.setVisibility(View.VISIBLE);
                    binding.create.setEnabled(false);
                    return;
                }
                binding.amountError.setVisibility(View.GONE);
                binding.create.setEnabled(true);
                mViewModel.setAmount(new BigDecimal(s.toString()));
            }
        });

        binding.setSetLabel(new TextViewBindingAdapter.OnTextChanged() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setLabel(s.toString());
            }
        });

        binding.operationDateText.setOnClickListener(operationDateListener());
        binding.operationDateButton.setOnClickListener(operationDateListener());

        binding.valueDateText.setOnClickListener(valueDateListener());
        binding.valueDateButton.setOnClickListener(valueDateListener());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OperationViewModel.class);
        mViewModel.setOperationDao(AppDatabase.get().getOperationDao());
        mViewModel.setAccountDao(AppDatabase.get().getAccountDao());
        long accountId = OperationCreatorFragmentArgs.fromBundle(getArguments()).getId();
        mViewModel.setAccountId(accountId);
        long maxAmount = OperationCreatorFragmentArgs.fromBundle(getArguments()).getMaxAmount();
        mViewModel.setMaxAmount(maxAmount);
        mViewModel.createOperation();
        mViewModel.getAmount().observe(getViewLifecycleOwner(), a -> binding.amountEdit.setText(a.toString()));
        mViewModel.getOperationDate().observe(getViewLifecycleOwner(), o -> binding.operationDateButton.setText(new SimpleDateFormat("dd/MM/yyyy").format(o)));
        mViewModel.getValueDate().observe(getViewLifecycleOwner(), o -> binding.valueDateButton.setText(new SimpleDateFormat("dd/MM/yyyy").format(o)));


        binding.setVm(mViewModel);
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point point = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(point);
        window.setLayout((int) (point.x * 0.75), (int) (point.y * 0.65));
        window.setGravity(Gravity.CENTER);
        super.onResume();
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

    public void setHideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
