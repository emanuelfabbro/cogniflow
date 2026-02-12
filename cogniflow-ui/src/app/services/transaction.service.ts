import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Definimos la interfaz aquí mismo para simplificar
export interface TransactionRequest {
    accountId: string;
    amount: number;
    currency: string;
}

export interface TransactionResponse {
    id: string;
    status: string;
    amount: number;
}

@Injectable({
    providedIn: 'root'
})
export class TransactionService {
    private apiUrl = 'http://localhost:8081/api/v1/transactions';

    constructor(private http: HttpClient) { }

    createTransaction(transaction: TransactionRequest): Observable<TransactionResponse> {
        return this.http.post<TransactionResponse>(this.apiUrl, transaction);
    }
}