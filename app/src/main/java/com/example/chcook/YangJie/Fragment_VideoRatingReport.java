package com.example.chcook.YangJie;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.chcook.R;
import com.example.chcook.YangJie.DA.PdfDocumentAdapter;
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

public class Fragment_VideoRatingReport extends Fragment {
    private Button report;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videoratingreport, container, false);
        report = view.findViewById(R.id.btnCreateReport);
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        report.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                createPDFfile(Common.getAppPath(v.getContext())+"test_pdf.pdf");
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDFfile(String appPath) {
        if(new File(appPath).exists())
            new File(appPath).delete();
        try{
            Document document = new Document();
            //save
            PdfWriter.getInstance(document, new FileOutputStream(appPath));
            //Open to write
            document.open();

            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Author name");
            document.addCreator("Creator Name");

            //Font Setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.2f;
            float valueFontSize = 26.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf","UTF-8",BaseFont.EMBEDDED);

            //Create Title of Document
            Font titleFont  = new Font(fontName,36.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Details", Element.ALIGN_CENTER,titleFont);

            //Add more
            Font orderNumberFont = new Font (fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,"OrderNo:", Element.ALIGN_LEFT,orderNumberFont);

            Font orderNumberVFont = new Font (fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"123456", Element.ALIGN_LEFT,orderNumberVFont);

            addLineSeperator(document);

            addNewItem(document,"Order Date",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,"2/2/2222",Element.ALIGN_LEFT,orderNumberVFont);

            addLineSeperator(document);

            addNewItem(document,"Account Name:",Element.ALIGN_LEFT,orderNumberFont);
            addNewItem(document,"Jacky",Element.ALIGN_LEFT,orderNumberVFont);

            addLineSeperator(document);

            //add product order detail
            addLineSpace(document);
            addNewItem(document,"Product Detail",Element.ALIGN_CENTER,titleFont);
            addLineSeperator(document);

            //Item 1
            addNewItemWithLeftAndRight(document,"Pizza 2","(0.0%)",titleFont,orderNumberVFont);
            addNewItemWithLeftAndRight(document,"120.*1000","12000.0",titleFont,orderNumberVFont);

            addLineSeperator(document);

            //Item 2
            addNewItemWithLeftAndRight(document,"Pizza 2","(0.0%)",titleFont,orderNumberVFont);
            addNewItemWithLeftAndRight(document,"120.*1000","12000.0",titleFont,orderNumberVFont);

            addLineSeperator(document);

            //Total
            addLineSpace(document);
            addLineSpace(document);

            addNewItemWithLeftAndRight(document,"Total:","24000.0",titleFont,orderNumberVFont);

            document.close();
            Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();

            printPDF();



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
        PrintManager printManager = (PrintManager)getActivity().getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(getContext(),Common.getAppPath(getContext())+"test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter, new PrintAttributes.Builder().build());
        }catch(Exception ex){
            Log.e("EDMTDev",""+ex.getMessage());
        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont ,Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft,textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight,textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);

    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int alignCenter, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(alignCenter);
        document.add(paragraph);
    }
}
