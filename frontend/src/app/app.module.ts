import {BrowserModule} from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatDialogModule,
    MatDividerModule, MatFormFieldControl,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatSelectModule,
    MatSidenavModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule
} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
    DynamicFormComponent,
    FormSubmissionDialogComponent
} from './dynamic-form/dynamic-form.component';
import {DynamicFormQuestionComponent} from './dynamic-form-question/dynamic-form-question.component';
import {LoginComponent} from './login/login.component';
import {PrivacyComponent} from './privacy/privacy.component';
import {EventComponent} from './event/event.component';
import {MediaMatcher} from '@angular/cdk/layout';
import {ConfigService} from './config/config.service';
import {ExportComponent} from './export/export.component';
import {TokenInterceptor} from './auth/token-interceptor.service';
import {AuthService} from './auth/auth.service';
import {AuthGuardService} from './auth/auth-guard.service';
import {EventTemplateEntityService} from './api/eventTemplateEntity.service';
import {LocationEntityService} from './api/locationEntity.service';
import {UserEntityService} from './api/userEntity.service';
import {ReportsComponent} from './reports/reports.component';
import {ReportsService} from './reports/reports.service';
import {AboutComponent} from './about/about.component';
import {LocationComponent} from './location/location.component';
import {LocationEditComponent} from './location-edit/location-edit.component';
import {UserEmailService} from './user/service/user-email.service';
import {
    ResetPasswordComponent,
    SuccessfullyResetPasswordComponent
} from './reset-password/reset-password.component';
import {
    ForgotPasswordComponent,
    SuccessfullySentPasswordRecoveryEmailComponent
} from './forgot-password/forgot-password.component';
import {SelectLocationComponent} from './select-location/select-location.component';
import {ParentLocationFinder} from './location/get-parent-locations';
import { UserComponent,
    UserSubmissionSuccessDialogComponent,
    UserSubmissionFailureDialogComponent
} from './user/user.component';
import {ReportsChartService} from './reports/reports-chart.service';

@NgModule({
    declarations: [
        AppComponent,
        DynamicFormComponent,
        DynamicFormQuestionComponent,
        LoginComponent,
        PrivacyComponent,
        EventComponent,
        ExportComponent,
        ReportsComponent,
        FormSubmissionDialogComponent,
        LocationComponent,
        LocationEditComponent,
        SuccessfullySentPasswordRecoveryEmailComponent,
        SuccessfullyResetPasswordComponent,
        AboutComponent,
        ForgotPasswordComponent,
        ResetPasswordComponent,
        SelectLocationComponent,
        UserComponent,
        UserSubmissionSuccessDialogComponent,
        UserSubmissionFailureDialogComponent
    ],
    imports: [
        BrowserModule,
        HttpClientModule,
        AppRoutingModule,
        MatFormFieldModule,
        MatInputModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatCheckboxModule,
        MatDividerModule,
        MatButtonModule,
        MatDialogModule,
        MatIconModule,
        ReactiveFormsModule,
        MatToolbarModule,
        MatSidenavModule,
        MatListModule,
        MatTabsModule,
        MatSelectModule,
        MatProgressBarModule,
        MatDialogModule,
        MatTableModule,
        MatPaginatorModule
    ],
    entryComponents: [
        FormSubmissionDialogComponent,
        SuccessfullySentPasswordRecoveryEmailComponent,
        SuccessfullyResetPasswordComponent,
        UserSubmissionSuccessDialogComponent,
        UserSubmissionFailureDialogComponent
    ],
    providers: [
        MediaMatcher,
        ConfigService,
        AuthService,
        AuthGuardService,
        UserEmailService, {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true
        },
        ReportsService,
        EventTemplateEntityService,
        LocationEntityService,
        ParentLocationFinder,
        ReportsChartService,
        UserEntityService
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
