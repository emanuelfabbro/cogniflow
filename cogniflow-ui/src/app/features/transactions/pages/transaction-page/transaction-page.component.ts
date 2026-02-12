import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TransactionFormComponent } from '../../components/transaction-form/transaction-form.component';
import { TransactionService, TransactionRequest, TransactionResponse } from '../../../../services/transaction.service';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';

@Component({
  selector: 'app-transaction-page',
  standalone: true,
  imports: [CommonModule, TransactionFormComponent],
  template: `
    <div class="container py-5">
      <div class="row justify-content-center">
        <div class="col-md-6">
          
          <div class="card shadow-lg border-0 rounded-4">
            <div class="card-header bg-dark text-white text-center py-3 rounded-top-4">
              <h4 class="mb-0">CogniFlow Fraud Detector</h4>
            </div>
            
            <div class="card-body p-4">
              <app-transaction-form (formSubmit)="handleTransaction($event)"></app-transaction-form>
              
              <div *ngIf="isLoading" class="text-center mt-4">
                <div class="spinner-border text-primary" role="status"></div>
                <p class="mt-2 text-muted">Consultando IA de Fraude...</p>
              </div>

              <div *ngIf="errorMessage" class="alert alert-danger mt-3 d-flex align-items-center">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <div>{{ errorMessage }}</div>
              </div>

              <div *ngIf="transactionResult" class="mt-4 animate__animated animate__fadeIn">
                <div class="alert" 
                     [ngClass]="{'alert-success': transactionResult.status === 'APPROVED', 
                                 'alert-danger': transactionResult.status === 'FRAUD_DETECTED'}">
                  <h5 class="alert-heading fw-bold">
                    {{ transactionResult.status === 'APPROVED' ? '✅ APROBADA' : '🚨 FRAUDE DETECTADO' }}
                  </h5>
                  <hr>
                  <p class="mb-0">ID: <small class="text-monospace">{{ transactionResult.id }}</small></p>
                </div>
              </div>

            </div>
          </div>

        </div>
      </div>
    </div>
  `
})
export class TransactionPageComponent {
  isLoading = false;
  errorMessage = '';
  transactionResult: TransactionResponse | null = null;

  constructor(private transactionService: TransactionService) { }

  handleTransaction(request: TransactionRequest) {
    this.isLoading = true;
    this.errorMessage = '';
    this.transactionResult = null;

    this.transactionService.createTransaction(request)
      .pipe(
        catchError(error => {
          this.errorMessage = 'Error conectando con el servidor. Intente más tarde.';
          console.error(error);
          return of(null);
        }),
        finalize(() => this.isLoading = false)
      )
      .subscribe(response => {
        if (response) {
          this.transactionResult = response;
        }
      });
  }
}