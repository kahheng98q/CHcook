package com.example.chcook.YangJie;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.chcook.Domain.Payment;
import com.example.chcook.R;
import com.example.chcook.YangJie.DA.PdfDocumentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_IncomeReport extends Fragment {
    private Button report;
    private Spinner ySpinner, mSpinner;
    private ProgressBar pg;
    private Boolean gotData=false;
    private ArrayList<Payment> payments = new ArrayList<>();
    private ArrayList<Payment> firstPayments = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income_report, container, false);
//        payments.clear();
        report = view.findViewById(R.id.btnCreateReport);
        pg = view.findViewById(R.id.progressBarReport1);
        ySpinner = view.findViewById(R.id.spinnerIncomeReportYear);
        mSpinner = view.findViewById(R.id.spinnerIncomeReportMonth);
        String[] arrayYear = new String[]{"2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031"};
        String[] arrayMonth = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.style_spinner, arrayYear);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(adapter);
        final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), R.layout.style_spinner, arrayMonth);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        final Handler handler = new Handler(Looper.getMainLooper());

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        report.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(final View v) {
                                payments.clear();
                                report.setText("Loading...");
                                ySpinner.setVisibility(View.INVISIBLE);
                                mSpinner.setVisibility(View.INVISIBLE);
                                report.setEnabled(false);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Payment");
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (final DataSnapshot income : dataSnapshot.getChildren()) {
                                                Long dd = income.child("PayDate").getValue(Long.class);
                                                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                                                String latestDate = df.format(dd);

                                                if (latestDate.equals(mSpinner.getSelectedItem().toString() + "-" + ySpinner.getSelectedItem().toString())) {
                                                    gotData=true;
                                                    Long dato = income.child("PayDate").getValue(Long.class);
                                                    SimpleDateFormat ddf = new SimpleDateFormat("dd-MM-yyyy");
                                                    final String getLastDate = ddf.format(dato);
                                                    final Integer price = income.child("Price").getValue(Integer.class);
                                                    String userId = income.child("UserId").getValue(String.class);





                                                        DatabaseReference query = FirebaseDatabase.getInstance().getReference("Users").child(userId);
//
                                                        query.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot ds) {

                                                                String userName = ds.child("Name").getValue(String.class);
                                                                String userEmail = ds.child("Email").getValue(String.class);
//                                                payments.add(new Payment("111",22,"333","444"));
                                                                payments.add(new Payment(getLastDate, price, userName, userEmail));

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                                }

                                            }
                                            pg.setVisibility(View.INVISIBLE);
                                        }
//                pg.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(gotData.equals(true)){
                                                createPDFfile(Common.getAppPath(v.getContext())+"test_pdf.pdf");
                                            }else{
                                            Toast.makeText(getActivity(),"no record found",Toast.LENGTH_SHORT).show();
                                        }
                                            ySpinner.setVisibility(View.VISIBLE);
                                            mSpinner.setVisibility(View.VISIBLE);
                                            report.setText("Create Report");
                                            report.setEnabled(true);
                                        }
                                    }, 10000);




                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                })
                .check();


        return view;
    }

    private void getFirstData() {
        pg.setVisibility(View.VISIBLE);
        firstPayments.clear();
        payments.clear();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Payment");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot income : dataSnapshot.getChildren()) {
                        Long dd = income.child("PayDate").getValue(Long.class);
                        SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                        String latestDate = df.format(dd);

                        if (latestDate.equals(mSpinner.getSelectedItem().toString() + "-" + ySpinner.getSelectedItem().toString())) {
                            Long dato = income.child("PayDate").getValue(Long.class);
                            SimpleDateFormat ddf = new SimpleDateFormat("dd-MM-yyyy");
                            final String getLastDate = ddf.format(dato);
                            final Integer price = income.child("Price").getValue(Integer.class);
                            String userId = income.child("UserId").getValue(String.class);
                            firstPayments.add(new Payment(getLastDate, price, userId));


                        }

                    }
                    pg.setVisibility(View.INVISIBLE);
                }
//                pg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDFfile(String appPath) {
        if (new File(appPath).exists())
            new File(appPath).delete();
        try {
            pg.setVisibility(View.INVISIBLE);
            final Document document = new Document();
            //save
            PdfWriter.getInstance(document, new FileOutputStream(appPath));
            //Open to write
            document.open();
            final Integer[] count = {0};
            final Integer[] count2 = {0};
            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Author name");
            document.addCreator("Creator Name");

            //Font Setting
            BaseColor colorAccent = new BaseColor(0, 153, 204, 1);
            BaseColor blackColor = new BaseColor(0, 0, 0, 255);

            float fontSize = 13f;
            float valueFontSize = 13.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            //Title
            Font titleFont = new Font(fontName, 33.0f, Font.NORMAL, BaseColor.BLACK);
            Font titleFont2 = new Font(fontName, 22.0f, Font.NORMAL, BaseColor.BLACK);
            Font space = new Font(fontName, 16.0f, Font.NORMAL, BaseColor.BLACK);


            addNewItem(document, "Income Report", Element.ALIGN_CENTER, titleFont);
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);

            //Print Date
            Date currentTime = Calendar.getInstance().getTime();
            final Font wordFont = new Font(fontName, fontSize, Font.NORMAL, blackColor);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentdate = df.format(currentTime);
            addNewItem(document, "Printed Date: " + currentdate, Element.ALIGN_LEFT, wordFont);
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);
            Font orderNumberVFont = new Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK);

            addNewItem(document, "Selected Year : " + ySpinner.getSelectedItem().toString(), Element.ALIGN_LEFT, wordFont);
            String month="";
            switch (mSpinner.getSelectedItem().toString()){
                    case "01":
                        month="January";
                        break;
                    case "02":
                        month="February";
                        break;
                    case "03":
                        month="March";
                        break;
                    case "04":
                        month="April";
                        break;
                    case "05":
                        month="May";
                        break;
                    case "06":
                        month="June";
                        break;
                    case "07":
                        month="July";
                        break;
                    case "08":
                        month="August";
                        break;
                    case "09":
                        month="September";
                        break;
                    case "10":
                        month="October";
                        break;
                    case "11":
                        month="November";
                        break;
                    case "12":
                        month="December";
                        break;
                }



            addNewItem(document, "Selected Month : " + month, Element.ALIGN_LEFT, wordFont);
            addNewItem(document, "\n", Element.ALIGN_CENTER, titleFont2);
            //title
            addLineSeperator(document);
            String stitle = String.format("%s%17s%17s%13s", "Date", "Price", "UserName", "Email");
            addNewItem(document, stitle, Element.ALIGN_LEFT, titleFont2);
            addLineSeperator(document);
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);
            Integer total=0;
            //Item
            for (int i = 0; i < payments.size(); i++) {
                String item = String.format("%10s%20d%35s%40s", payments.get(i).getPayDate(), payments.get(i).getPrice(), payments.get(i).getUserName(), payments.get(i).getUserEmail());
                addNewItem(document, item, Element.ALIGN_LEFT, wordFont);
                total = total+payments.get(i).getPrice();

            }

            //Total
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);
            addNewItem(document, "Record Found : " + payments.size(), Element.ALIGN_LEFT, wordFont);
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);
            addNewItem(document, "\n", Element.ALIGN_CENTER, space);
            addNewItem(document, "Total amount : RM "+total, Element.ALIGN_LEFT, wordFont);
            Integer totalCount = count[0] - count2[0];
//            if(totalCount==2) {
            document.close();
            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

            printPDF();
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() {
        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(getContext(), Common.getAppPath(getContext()) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception ex) {
            Log.e("EDMTDev", "" + ex.getMessage());
        }
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_staff, new MainFragment_staff());
//        fragmentTransaction.commit();
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);

    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int alignCenter, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);
        document.add(paragraph);
    }
}
