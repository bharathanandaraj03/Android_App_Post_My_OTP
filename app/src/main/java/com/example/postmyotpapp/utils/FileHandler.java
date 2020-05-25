package com.example.postmyotpapp.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHandler extends AppCompatActivity {

    // This method will read data from FileInputStream.
    public void readDataFromFile(Context ctx,String fileName) {
        try {
            FileInputStream fileInputStream = ctx.openFileInput(fileName);
            StringBuffer retBuf = new StringBuffer();

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String lineData = bufferedReader.readLine();
            while (lineData != null) {
                retBuf.append(lineData);
                lineData = bufferedReader.readLine();
            }
            String fileData = retBuf.toString();

            if (fileData.length() > 0) {
                Toast.makeText(ctx, "Load saved data complete. "+fileData, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ctx, "Not load any data.", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException ex) {
            Log.e("TAG_WRITE_READ_FILE", ex.getMessage(), ex);
        }catch(IOException ex)
        {
            Log.e("TAG_WRITE_READ_FILE", ex.getMessage(), ex);
        }
    }

    // This method will write data to FileOutputStream.
    public void writeDataToFile(Context ctx,String fileName, String data){
        try
        {
            FileOutputStream fileOutputStream = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
            Toast.makeText(ctx, "Data has been written to file " + fileName, Toast.LENGTH_LONG).show();

        }catch(FileNotFoundException ex)
        {
            Log.e("TAG_WRITE_READ_FILE", ex.getMessage(), ex);
        }catch(IOException ex)
        {
            Log.e("TAG_WRITE_READ_FILE", ex.getMessage(), ex);
        }

    }
}
