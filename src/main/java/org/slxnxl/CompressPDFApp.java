package org.slxnxl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdfwriter.compress.CompressParameters;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

class CompressPDFApp {
    private String filePath;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("PDF Compressor");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            new CompressPDFApp().initialize(frame);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void initialize(JFrame frame) {
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JButton browseButton = new JButton("Обзор");
        browseButton.addActionListener(e -> browseFile());
        frame.add(browseButton);

        JLabel filenameLabel = new JLabel("");
        frame.add(filenameLabel);

        JLabel qualityLabel = new JLabel("Выберите качество изображения:");
        frame.add(qualityLabel);

        JSlider qualitySlider = new JSlider(0, 100, 50);
        frame.add(qualitySlider);

        JLabel qualityLabel2 = new JLabel("Выберите степень сжатия файла (от 0 до 1000):");
        frame.add(qualityLabel2);

        JSlider qualitySlider2 = new JSlider(0, 1000, 500);
        frame.add(qualitySlider2);

        JButton compressButton = new JButton("Сжать");
        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filePath == null || filePath.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Выберите файл для сжатия.");
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выберите место сохранения файла");
                int userSelection = fileChooser.showSaveDialog(frame);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    try {
                        compressPDF(fileToSave.getAbsolutePath(), qualitySlider.getValue(), qualitySlider2.getValue());
                        JOptionPane.showMessageDialog(frame, "Файл успешно сжат и сохранен.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Ошибка при сжатии и сохранении файла: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        frame.add(compressButton);
    }

private void browseFile() {
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF files", "pdf");
    fileChooser.setFileFilter(filter);

    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        filePath = selectedFile.getAbsolutePath();
        JOptionPane.showMessageDialog(null, "Выбран файл: " + filePath);
    }};

private void compressPDF(String outputPath, int imageQuality, int compressionLevel) throws IOException {
    File file = new File(filePath);
    PDDocument document = Loader.loadPDF(file);
    CompressEngine.compressImages(document, imageQuality);
    outputPath += ".pdf";
    document.save(outputPath, new CompressParameters(compressionLevel));
    document.close();
}
}
