/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.graphene.test;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ActionMapUIResource;

/**
 *
 * @author carcassi
 */
public class CompareResultImages extends javax.swing.JFrame {
    
    private TestResultManager manager = new TestResultManager();
    private Action acceptAction = new AbstractAction("Accept") {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = toReviewList.getSelectedIndex();
            TestResultManager.Result result = (TestResultManager.Result) toReviewList.getSelectedValue();
            result.accept();
            fillList();
            if (index >= toReviewList.getModel().getSize()) {
                index = toReviewList.getModel().getSize() - 1;
            }
            toReviewList.clearSelection();
            toReviewList.setSelectedIndex(index);
        }
    };

    /**
     * Creates new form CompareResultImages
     */
    public CompareResultImages() {
        initComponents();
        toReviewList.setModel(new DefaultListModel());
        fillList();
        setSize(800, 600);
        setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
        actualImage.setStretch(false);
        referenceImage.setStretch(false);
        acceptAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control D"));
        
        InputMap keyMap = new ComponentInputMap(acceptButton);
        keyMap.put(KeyStroke.getKeyStroke("control D"), "accept");

        ActionMap actionMap = new ActionMapUIResource();
        actionMap.put("accept", acceptAction);

        SwingUtilities.replaceUIActionMap(acceptButton, actionMap);
        SwingUtilities.replaceUIInputMap(acceptButton, JComponent.WHEN_IN_FOCUSED_WINDOW, keyMap);
    }
    
    private void fillList() {
        DefaultListModel newModel = (DefaultListModel) toReviewList.getModel();
        newModel.clear();
        for (TestResultManager.Result result : manager.getCurrentResults()) {
            newModel.addElement(result);
        }
//        toReviewList.setModel(newModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        actualImage = new org.epics.graphene.ImagePanel();
        Reference = new javax.swing.JLabel();
        referenceImage = new org.epics.graphene.ImagePanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        toReviewList = new javax.swing.JList();
        acceptButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Actual:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanel1.add(actualImage, gridBagConstraints);

        Reference.setText("Reference:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanel1.add(Reference, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        jPanel1.add(referenceImage, gridBagConstraints);

        jSplitPane1.setRightComponent(jPanel1);

        toReviewList.setPreferredSize(new java.awt.Dimension(250, 0));
        toReviewList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                toReviewListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(toReviewList);

        acceptButton.setAction(acceptAction);
        acceptButton.setText("Accept");

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(acceptButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptButton)
                    .addComponent(refreshButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 821, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void toReviewListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_toReviewListValueChanged
        TestResultManager.Result result = (TestResultManager.Result) toReviewList.getSelectedValue();
        if (result == null) {
            actualImage.setImage(null);
            referenceImage.setImage(null);
            return;
        }
        
        if (result.getFailedImage().exists()) {
            try {
                actualImage.setImage(ImageIO.read(result.getFailedImage()));
            } catch (IOException ex) {
                Logger.getLogger(CompareResultImages.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            actualImage.setImage(null);
        }
        if (result.getReferenceImage().exists()) {
            try {
                referenceImage.setImage(ImageIO.read(result.getReferenceImage()));
            } catch (IOException ex) {
                Logger.getLogger(CompareResultImages.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            referenceImage.setImage(null);
        }

    }//GEN-LAST:event_toReviewListValueChanged

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        manager.refresh();
        fillList();
    }//GEN-LAST:event_refreshButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CompareResultImages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CompareResultImages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CompareResultImages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CompareResultImages.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CompareResultImages().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Reference;
    private javax.swing.JButton acceptButton;
    private org.epics.graphene.ImagePanel actualImage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private org.epics.graphene.ImagePanel referenceImage;
    private javax.swing.JButton refreshButton;
    private javax.swing.JList toReviewList;
    // End of variables declaration//GEN-END:variables
}
