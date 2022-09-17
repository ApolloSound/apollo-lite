import { NgModule } from '@angular/core';
import { DeveloperComponent } from './developer.component';
import { ApolloSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { DEVELOPER_ROUTE } from 'app/developer/developer.route';

@NgModule({
  declarations: [DeveloperComponent],
  imports: [ApolloSharedModule, RouterModule.forChild([DEVELOPER_ROUTE])]
})
export class DeveloperModule {}
