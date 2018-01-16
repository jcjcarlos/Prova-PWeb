package rede;

import java.util.ArrayList;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Servico implements Runnable {
	byte[] vetor;
	Socket cliente;
	
	public Servico(Socket cliente) {
		this.cliente = cliente;
	}
	
	private void retornaPilha (byte[] tempVet) {
	
	byte[] newVet = new byte[tempVet.length];
	int par = 0;
	
	for(int i=0;i<tempVet.length;i++) {
		if(tempVet[i]%2==0) {
			newVet[par] = tempVet[i];
			par++;
		}
	}
	
	for(int i=par;i<tempVet.length;i++)
		newVet[i] = tempVet[i];
		
		
	this.vetor = newVet;
	}
	
	private void removePar(byte[] tempVet) {
		int tam = 0;
		for(byte b: tempVet)
			if(b%2!=0)
				tam++;
		byte[] newVet = new byte[tam];
		
		for (int i=0,j=0;i<tam;i++)
			if(tempVet[i]%2!=0) {
				newVet[j]=tempVet[i];
				j++;
			}
		this.vetor = newVet;
	}
	
	private int contaImpar(byte[] tempVet) {
		int i = 0;
		for(byte b: tempVet)
			if(b%2!=0)
				i++;
		return i;
	}
	
	private int produtoEscalar(byte[] vet1, byte[] vet2) {
		int limite,resultado = 0;
		if(vet1.length<vet2.length)
			limite = vet1.length;
		else limite = vet2.length;
		byte[] newVet = new byte[limite];
		
		for(int i = 0;i<limite;i++)
			resultado+= vet1[i] * vet2[i];
		return resultado;
	}
	
	public void run() {
		try {
			DataInputStream in = new DataInputStream(this.cliente.getInputStream());
			DataOutputStream out = new DataOutputStream(this.cliente.getOutputStream());
			int opc = in.read();
			vetor = new byte[in.read()];
			in.read(vetor);
			
			switch(opc) {
				case 1: 
					retornaPilha(vetor);
					out.write(vetor);
					break;
				case 2: 
					removePar(vetor);
					out.write(vetor.length);
					out.flush();
					out.write(vetor);
					out.flush();
					break;
				case 3:
					out.write(contaImpar(vetor));
					out.flush();
					break;
				case 4: 
					byte[] vetor2 = new byte[in.read()];
					in.read(vetor2);
					out.write(produtoEscalar(vetor,vetor2));
					out.flush();
					break;
				default:
					System.out.println("Opcao invalida: "+opc);
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}

